package com.marvinlabs.widget.slideshow.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Collection;

/**
 * A GenericBitmapAdapter that loads images from application resources.
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 29/05/2014.
 */
public abstract class GenericResourceBitmapAdapter<T> extends GenericBitmapAdapter<T> {

    // The options to pass to the BitmapFactory used to decode the bitmaps
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
    public GenericResourceBitmapAdapter(Context context, Collection<T> items) {
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
    public GenericResourceBitmapAdapter(Context context, Collection<T> items, BitmapFactory.Options bitmapFactoryOptions) {
        super(context, items);
        this.bitmapFactoryOptions = bitmapFactoryOptions;
    }

    //==============================================================================================
    // ASYNC MANAGEMENT METHODS
    //==

    protected abstract int getItemImageResourceId(T item, int position);

    @Override
    protected Bitmap asyncLoadBitmap(T item, int position) {
        Bitmap bm = BitmapFactory.decodeResource(getContext().getResources(), getItemImageResourceId(item, position), bitmapFactoryOptions);
        return bm;
    }
}
