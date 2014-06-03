package com.marvinlabs.widget.slideshow.picasso;

import android.content.Context;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.Collection;

/**
 * An implementation of GenericPicassoBitmapAdapter to load application resources
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 29/05/2014.
 */
public class PicassoResourceBitmapAdapter extends GenericPicassoBitmapAdapter<Integer> {

    public PicassoResourceBitmapAdapter(Context context, Collection<Integer> items) {
        super(context, items);
    }

    @Override
    protected RequestCreator createRequestCreator(Picasso picasso, Integer resourceId) {
        return picasso.load(resourceId).noFade().skipMemoryCache();
    }

}
