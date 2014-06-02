package com.marvinlabs.widget.slideshow.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.marvinlabs.widget.slideshow.adapter.BitmapAdapter;
import com.squareup.picasso.Picasso;
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

    @Override
    protected void loadBitmap(final int position) {
        if (position < 0 || position >= slideUrls.size()) onBitmapNotAvailable(position);

        Picasso.with(getContext()).load(slideUrls.get(position)).noFade().into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                PicassoBitmapAdapter.this.onBitmapLoaded(position, bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable drawable) {
                PicassoBitmapAdapter.this.onBitmapNotAvailable(position);
            }

            @Override
            public void onPrepareLoad(Drawable drawable) {
            }
        });
    }

}
