package com.marvinlabs.widget.slideshow.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.util.Collection;

/**
 * A GenericBitmapAdapter that loads images from the Internet.
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 29/05/2014.
 */
public abstract class GenericRemoteBitmapAdapter<T> extends GenericBitmapAdapter<T> {

    // Options for the BitmapFactory to decode the bitmap
    private BitmapFactory.Options bitmapFactoryOptions;

    //==============================================================================================
    // GENERAL METHODS
    //==

    /**
     * Constructor
     *
     * @param context The context in which the adapter is created (activity)
     * @param items   The items for which we have images to load
     */
    public GenericRemoteBitmapAdapter(Context context, Collection<T> items) {
        super(context, items);
    }

    /**
     * Constructor
     *
     * @param context              The context in which the adapter is created (activity)
     * @param items                The items for which we have images to load
     * @param bitmapFactoryOptions The options to pass to the BitmapFactory used to decode the
     *                             bitmaps
     */
    public GenericRemoteBitmapAdapter(Context context, Collection<T> items, BitmapFactory.Options bitmapFactoryOptions) {
        super(context, items);
        this.bitmapFactoryOptions = bitmapFactoryOptions;
    }

    //==============================================================================================
    // ASYNC MANAGEMENT METHODS
    //==

    protected abstract String getItemImageUrl(T item, int position);

    @Override
    protected Bitmap asyncLoadBitmap(T item, int position) {
        InputStream in = null;
        try {
            in = new java.net.URL(getItemImageUrl(item, position)).openStream();
            Bitmap bm = BitmapFactory.decodeStream(in, null, bitmapFactoryOptions);
            return bm;
        } catch (Exception e) {
            Log.e("RemoteImageSlide", "Error while downloading image slide", e);
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e) { /* Ignored */ }
        }
        return null;
    }

}
