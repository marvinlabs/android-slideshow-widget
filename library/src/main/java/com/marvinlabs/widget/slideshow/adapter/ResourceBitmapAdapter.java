package com.marvinlabs.widget.slideshow.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * A BitmapAdapter that loads them from application resources.
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 29/05/2014.
 */
public class ResourceBitmapAdapter extends BitmapAdapter {

    private BitmapFactory.Options bitmapFactoryOptions;
    private int[] slideImageIds;

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
    }

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

    @Override
    protected void loadBitmap(int position) {
        if (position < 0 || position >= slideImageIds.length) onBitmapNotAvailable(position);

        Bitmap bm = BitmapFactory.decodeResource(getContext().getResources(), slideImageIds[position], bitmapFactoryOptions);
        if (bm != null) {
            onBitmapLoaded(position, bm);
        } else {
            onBitmapNotAvailable(position);
        }
    }
}
