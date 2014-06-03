package com.marvinlabs.widget.slideshow.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;

import com.marvinlabs.widget.slideshow.adapter.BitmapAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A BitmapAdapter that loads them from the Internet using AsyncTasks. Don't forget to call the
 * adapter's #stopAllDownloads method when the activity gets stopped. Failing to do so may leave you with
 * running AsyncTask instances.
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 29/05/2014.
 */
public class PicassoBitmapAdapter extends BitmapAdapter {

    // URLs of the images to load
    private List<String> slideUrls;

    // Targets currently in use by Picasso
    private SparseArray<SlideTarget> activeTargets;

    //==============================================================================================
    // GENERAL METHODS
    //==

    /**
     * Constructor
     *
     * @param context   The context in which the adapter is created (activity)
     * @param slideUrls The URLs of the images to load
     */
    public PicassoBitmapAdapter(Context context, Collection<String> slideUrls) {
        super(context);
        this.slideUrls = new ArrayList<String>(slideUrls);
        this.activeTargets = new SparseArray<SlideTarget>(3);
    }

    //==============================================================================================
    // INTERFACE IMPLEMENTATION: Adapter
    //==

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

    //==============================================================================================
    // BITMAP LOADING
    //==

    /**
     * Stop all running download tasks. This method should be called when your activity gets
     * stopped (in {#onStop})
     */
    public void shutdown() {
        activeTargets.clear();
    }

    @Override
    protected void onBitmapLoaded(int position, Bitmap bitmap) {
        activeTargets.remove(position);
        super.onBitmapLoaded(position, bitmap);
    }

    @Override
    protected void onBitmapNotAvailable(int position) {
        activeTargets.remove(position);
        super.onBitmapNotAvailable(position);
    }

    @Override
    protected void loadBitmap(final int position) {
        if (position < 0 || position >= slideUrls.size()) onBitmapNotAvailable(position);

        SlideTarget target = new SlideTarget(position);
        activeTargets.put(position, target);

        RequestCreator rc = createRequestCreator(Picasso.with(getContext()), slideUrls.get(position));
        rc.into(target);
    }

    /**
     * Create the Picasso request. Subclasses can customize it by simply overriding this method. By
     * default, we use noFade() and skipMemoryCache()
     *
     * @param picasso The picasse instance to use
     * @param url     The URL of the image to load
     * @return The request creator object from Picasso
     */
    protected RequestCreator createRequestCreator(Picasso picasso, String url) {
        return picasso.load(url).noFade().skipMemoryCache();
    }

    /**
     * A target for Picasso to load the bitmap into
     */
    private class SlideTarget implements Target {
        int position;

        private SlideTarget(int position) {
            this.position = position;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, com.squareup.picasso.Picasso.LoadedFrom loadedFrom) {
            PicassoBitmapAdapter.this.onBitmapLoaded(position, bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable drawable) {
            PicassoBitmapAdapter.this.onBitmapNotAvailable(position);
        }

        @Override
        public void onPrepareLoad(Drawable drawable) {
        }
    }

}
