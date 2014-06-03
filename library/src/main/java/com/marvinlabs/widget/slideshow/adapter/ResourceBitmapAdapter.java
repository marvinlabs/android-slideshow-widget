package com.marvinlabs.widget.slideshow.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;

import java.util.Collection;

/**
 * A GenericBitmapAdapter that loads images from application resources.
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 29/05/2014.
 */
public class ResourceBitmapAdapter extends GenericResourceBitmapAdapter<Integer> {

    /**
     * Constructor
     *
     * @param context     The context in which the adapter is created (activity)
     * @param resourceIds The resourceIds for which we have images to load
     */
    public ResourceBitmapAdapter(Context context, Collection<Integer> resourceIds) {
        super(context, resourceIds);
    }

    /**
     * Constructor
     *
     * @param context              The context in which the adapter is created (activity)
     * @param resourceIds          The resourceIds for which we have images to load
     * @param bitmapFactoryOptions The options to pass to the BitmapFactory used to decode the
     *                             bitmaps
     */
    public ResourceBitmapAdapter(Context context, Collection<Integer> resourceIds, BitmapFactory.Options bitmapFactoryOptions) {
        super(context, resourceIds, bitmapFactoryOptions);
    }

    protected int getItemImageResourceId(Integer resourceId, int position) {
        return resourceId;
    }
}
