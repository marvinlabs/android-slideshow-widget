package com.marvinlabs.widget.slideshow.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import com.google.common.collect.Lists;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * A BitmapAdapter that loads them from the Internet using AsyncTasks. Don't forget to call the
 * adapter's #stopAllDownloads method when the activity gets stopped. Failing to do so may leave you with
 * running AsyncTask instances.
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 29/05/2014.
 */
public class RemoteBitmapAdapter extends BitmapAdapter {

    // URLs of the images to load
    private List<String> slideUrls;

    // A pool of running AsyncTasks
    private SparseArray<DownloadImageTask> runningTasks;

    // Options for the BitmapFactory to decode the bitmap
    private BitmapFactory.Options bitmapFactoryOptions;

    /**
     * Constructor
     *
     * @param context   The context in which the adapter is created (activity)
     * @param slideUrls The URLs of the images to load
     */
    public RemoteBitmapAdapter(Context context, Collection<String> slideUrls) {
        this(context, slideUrls, null);
    }

    /**
     * Constructor
     *
     * @param context              The context in which the adapter is created (activity)
     * @param slideUrls            The URLs of the images to load
     * @param bitmapFactoryOptions The options to pass to the BitmapFactory used to decode the
     *                             bitmaps
     */
    public RemoteBitmapAdapter(Context context, Collection<String> slideUrls, BitmapFactory.Options bitmapFactoryOptions) {
        super(context);
        this.bitmapFactoryOptions = bitmapFactoryOptions;
        this.slideUrls = Lists.newArrayList(slideUrls);
        this.runningTasks = new SparseArray<DownloadImageTask>(5);
    }

    /**
     * Stop all running download tasks. This method should be called when your activity gets
     * stopped (in {#onStop})
     */
    public void stopAllDownloads() {
        final int taskCount = runningTasks.size();
        for (int i = 0; i < taskCount; ++i) {
            int key = runningTasks.keyAt(i);
            DownloadImageTask t = runningTasks.get(key);
            t.cancel(true);
        }
        runningTasks.clear();
    }

    @Override
    public int getCount() {
        return slideUrls.size();
    }

    @Override
    public String getItem(int position) {
        return slideUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    protected void loadBitmap(int position) {
        if (position < 0 || position >= slideUrls.size()) onBitmapNotAvailable(position);

        // If a task is already running, cancel it
        DownloadImageTask task = runningTasks.get(position);
        if (task != null) {
            task.cancel(true);
        }

        task = new DownloadImageTask(position);
        task.execute(slideUrls.get(position));
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

    /**
     * A task that downloads an image from the Internet and loads it into a bitmap
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        private int position;

        public DownloadImageTask(int position) {
            this.position = position;
        }

        protected Bitmap doInBackground(String... urls) {
            InputStream in = null;
            try {
                in = new java.net.URL(urls[0]).openStream();
                Bitmap bm = BitmapFactory.decodeStream(in, null, bitmapFactoryOptions);

                if (isCancelled()) {
                    if (bm != null) bm.recycle();
                    return null;
                }

                return bm;
            } catch (Exception e) {
                Log.e("RemoteImageSlide", "Error while downloading image slide", e);
            } finally {
                try {
                    if (in != null) in.close();
                } catch (Exception e) { /* Ignored */ }
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
