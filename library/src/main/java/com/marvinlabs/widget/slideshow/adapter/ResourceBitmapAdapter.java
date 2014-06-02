package com.marvinlabs.widget.slideshow.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

/**
 * A BitmapAdapter that loads them from application resources.
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 29/05/2014.
 */
public class ResourceBitmapAdapter extends BitmapAdapter {

    // The options to pass to the BitmapFactory used to decode the bitmaps
    private BitmapFactory.Options bitmapFactoryOptions;

    // A pool of running AsyncTasks
    private SparseArray<LoadResourceTask> runningTasks;

    // The resource IDs of the images to load
    private int[] slideImageIds;

    //==============================================================================================
    // GENERAL METHODS
    //==

    /**
     * Constructor
     *
     * @param context       The context in which the adapter is created (activity)
     * @param slideImageIds The resource IDs of the images to load
     */
    public ResourceBitmapAdapter(Context context, int[] slideImageIds) {
        this(context, slideImageIds, null);
    }

    /**
     * Constructor
     *
     * @param context              The context in which the adapter is created (activity)
     * @param slideImageIds        The resource IDs of the images to load
     * @param bitmapFactoryOptions The options to pass to the BitmapFactory used to decode the
     *                             bitmaps
     */
    public ResourceBitmapAdapter(Context context, int[] slideImageIds, BitmapFactory.Options bitmapFactoryOptions) {
        super(context);
        this.bitmapFactoryOptions = bitmapFactoryOptions;
        this.slideImageIds = slideImageIds;
        this.runningTasks = new SparseArray<LoadResourceTask>(5);
    }

    //==============================================================================================
    // INTERFACE IMPLEMENTATION: Adapter
    //==

    @Override
    public int getCount() {
        return slideImageIds.length;
    }

    @Override
    public Object getItem(int position) {
        return slideImageIds[position];
    }

    @Override
    public long getItemId(int position) {
        return slideImageIds[position];
    }

    //==============================================================================================
    // BITMAP LOADING
    //==

    @Override
    protected void loadBitmap(int position) {
        if (position < 0 || position >= slideImageIds.length) onBitmapNotAvailable(position);

        // If a task is already running, cancel it
        LoadResourceTask task = runningTasks.get(position);
        if (task != null) {
            task.cancel(true);
        }

        task = new LoadResourceTask(position);
        task.execute(slideImageIds[position]);
    }

    @Override
    protected void onBitmapLoaded(int position, Bitmap bitmap) {
        runningTasks.remove(position);
        super.onBitmapLoaded(position, bitmap);
    }

    @Override
    protected void onBitmapNotAvailable(int position) {
        runningTasks.remove(position);
        super.onBitmapNotAvailable(position);
    }

    //==============================================================================================
    // ASYNC MANAGEMENT METHODS
    //==

    /**
     * Stop all running download tasks. This method should be called when your activity gets
     * stopped (in {#onStop})
     */
    public void stopAllDownloads() {
        final int taskCount = runningTasks.size();
        for (int i = 0; i < taskCount; ++i) {
            int key = runningTasks.keyAt(i);
            LoadResourceTask t = runningTasks.get(key);
            t.cancel(true);
        }
        runningTasks.clear();
    }

    /**
     * A task that downloads an image from the Internet and loads it into a bitmap
     */
    private class LoadResourceTask extends AsyncTask<Integer, Void, Bitmap> {

        private int position;

        public LoadResourceTask(int position) {
            this.position = position;
        }

        protected Bitmap doInBackground(Integer... ids) {
            Log.d("SlideShowView", "Download started for item " + position);

            Bitmap bm = BitmapFactory.decodeResource(getContext().getResources(), ids[0], bitmapFactoryOptions);
            if (isCancelled()) {
                if (bm != null) bm.recycle();
                return null;
            }

            return bm;
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
