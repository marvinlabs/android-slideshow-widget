package com.sadeeq.app.widgets.slideshow.adapter.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseArray;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sadeeq.app.widgets.slideshow.adapter.BitmapAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Jarvis on 2/2/2017.
 */

public abstract class GenericGlideBitmapAdapter<T> extends BitmapAdapter {

    // URLs of the images to load
    private List<T> items;

    // Targets currently in use by Glide
    private SparseArray<GenericGlideBitmapAdapter.SlideTarget> activeTargets;

    /**
     * Constructor
     *
     * @param context The context that will hold the views
     */
    public GenericGlideBitmapAdapter(Context context, Collection<T> items) {
        super(context);
        this.items = new ArrayList<T>(items);
        this.activeTargets = new SparseArray<GenericGlideBitmapAdapter.SlideTarget>(3);
    }

    @Override
    protected void loadBitmap(final int position) {
        if (position < 0 || position >= items.size()) onBitmapNotAvailable(position);

        SlideTarget target = new SlideTarget(position);
        activeTargets.put(position, target);
        Glide.with(getContext()).load(items.get(position)).asBitmap().into(target);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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

    private class SlideTarget extends SimpleTarget<Bitmap> {
        int position;

        private SlideTarget(int position) {
            this.position = position;
        }


        @Override
        public void onLoadStarted(Drawable placeholder) {

        }

        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
            GenericGlideBitmapAdapter.this.onBitmapNotAvailable(position);
        }

        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            GenericGlideBitmapAdapter.this.onBitmapLoaded(position, resource);
        }


        @Override
        public void onLoadCleared(Drawable placeholder) {

        }


        @Override
        public void setRequest(Request request) {

        }

        @Override
        public Request getRequest() {
            return null;
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onStop() {

        }

        @Override
        public void onDestroy() {

        }


    }


}
