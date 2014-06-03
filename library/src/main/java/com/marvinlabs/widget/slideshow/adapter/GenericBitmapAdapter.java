package com.marvinlabs.widget.slideshow.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A BitmapAdapter that loads images asynchronously using AsyncTasks. Don't forget to call the
 * adapter's {#shutdown} method when the activity gets stopped. Failing to do so may leave you with
 * running AsyncTask instances.
 * <p/>
 * If your items are URLs or resource IDs, you can use our respective basic implementations:
 * RemoteBitmapAdapter or ResourceBitmapAdapter
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 29/05/2014.
 */
public abstract class GenericBitmapAdapter<T> extends BitmapAdapter {

    // URLs of the images to load
    private List<T> items;

    // A pool of running AsyncTasks
    private SparseArray<LoadBitmapTask> runningTasks;

    //==============================================================================================
    // GENERAL METHODS
    //==

    /**
     * Constructor
     *
     * @param context The context in which the adapter is created (activity)
     * @param items   The items for which we have images to load
     */
    public GenericBitmapAdapter(Context context, Collection<T> items) {
        super(context);
        this.items = new ArrayList<T>(items);
        this.runningTasks = new SparseArray<LoadBitmapTask>(3);
    }

    //==============================================================================================
    // INTERFACE IMPLEMENTATION: Adapter
    //==

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //==============================================================================================
    // BITMAP LOADING
    //==

    @Override
    protected void loadBitmap(int position) {
        if (position < 0 || position >= items.size()) onBitmapNotAvailable(position);

        // If a task is already running, cancel it
        LoadBitmapTask task = runningTasks.get(position);
        if (task != null) {
            task.cancel(true);
        }

        task = new LoadBitmapTask(position);
        task.execute(items.get(position));
    }

    @Override
    protected void onBitmapLoaded(int position, Bitmap bitmap) {
        Log.d("GenericBitmapAdapter", "Download finished for item " + position);
        runningTasks.remove(position);
        super.onBitmapLoaded(position, bitmap);
    }

    @Override
    protected void onBitmapNotAvailable(int position) {
        Log.d("GenericBitmapAdapter", "Download failed for item " + position);
        runningTasks.remove(position);
        super.onBitmapNotAvailable(position);
    }

    //==============================================================================================
    // ASYNC MANAGEMENT METHODS
    //==

    /**
     * Stop all running loading tasks. This method should be called when your activity gets
     * stopped (in {#onStop})
     */
    public void shutdown() {
        final int taskCount = runningTasks.size();
        for (int i = 0; i < taskCount; ++i) {
            int key = runningTasks.keyAt(i);
            LoadBitmapTask t = runningTasks.get(key);
            t.cancel(true);
        }
        runningTasks.clear();
    }

    /**
     * Subclasses shoudl implement this to load the bitmap on a background thread.
     *
     * @param item     The item for which we wish to load the bitmap
     * @param position The position of the item
     * @return A bitmap or null
     */
    protected abstract Bitmap asyncLoadBitmap(T item, int position);

    /**
     * A task that downloads an image from the Internet and loads it into a bitmap
     */
    private class LoadBitmapTask extends AsyncTask<T, Void, Bitmap> {

        private int position;

        public LoadBitmapTask(int position) {
            this.position = position;
        }

        protected Bitmap doInBackground(T... items) {
            Log.d("GenericBitmapAdapter", "Download started for item " + position);

            try {
                Bitmap bm = asyncLoadBitmap(items[0], position);

                if (isCancelled()) {
                    if (bm != null) bm.recycle();
                    return null;
                }

                return bm;
            } catch (Exception e) {
                Log.e("GenericBitmapAdapter", "Error while downloading image slide", e);
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                onBitmapLoaded(position, result);
            } else {
                onBitmapNotAvailable(position);
            }
        }
    }

}
