package com.sadeeq.app.widgets.slideshow.adapter.glide;

import android.content.Context;

import java.util.Collection;

/**
 * An implementation of GenericPicassoBitmapAdapter to load remote images from the Internet
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 29/05/2014.
 */
public class GlideRemoteBitmapAdapter extends GenericGlideBitmapAdapter<String> {

    public GlideRemoteBitmapAdapter(Context context, Collection<String> items) {
        super(context, items);
    }


}
