package com.marvinlabs.widget.slideshow.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.marvinlabs.widget.slideshow.SlideShowAdapter;

import java.lang.ref.WeakReference;

/**
 * Created by vprat on 29/05/2014.
 */
public class SimpleImageResourceAdapter extends BaseAdapter implements SlideShowAdapter {

    private Context context;
    private int[] slideImageIds;
    private SparseArray<WeakReference<Bitmap>> cachedBitmaps;

    public SimpleImageResourceAdapter(Context context, int[] slideImageIds) {
        this.context = context;
        this.slideImageIds = slideImageIds;
        this.cachedBitmaps = new SparseArray<WeakReference<Bitmap>>(slideImageIds.length);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv;

        if (convertView == null) {
            Log.d("SlideShowView", "No recycled view is available, creating a new one for slide at " + position);

            iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            iv = (ImageView) convertView;
        }

        WeakReference<Bitmap> bm = cachedBitmaps.get(position);
        if (bm==null || bm.get()==null || bm.get().isRecycled()) {
            prepareSlide(position);
            bm = cachedBitmaps.get(position);
        }

        if (getSlideStatus(position)==SlideStatus.READY) {
            iv.setImageBitmap(bm.get());
        }

        return iv;
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
    public void prepareSlide(int position) {
        Log.d("SlideShowView", "Preparing slide at position " + position);

        WeakReference<Bitmap> bm = cachedBitmaps.get(position);
        if (bm!=null && bm.get()!=null) {
            bm.get().recycle();
            bm.clear();
        }

        final Runtime runtime = Runtime.getRuntime();
        long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        Log.d("SlideShowView", "Memory used: " + usedMemInMB);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;

        bm = new WeakReference<Bitmap>(BitmapFactory.decodeResource(context.getResources(), slideImageIds[position], opts));
        cachedBitmaps.put(position, bm);

        Log.d("SlideShowView", "Cache now has this many bitmaps: " + cachedBitmaps.size());

        usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        Log.d("SlideShowView", "Memory used: " + usedMemInMB);
    }

    @Override
    public void discardSlide(int position) {
        Log.d("SlideShowView", "Discarding slide at position " + position);

        final Runtime runtime = Runtime.getRuntime();
        long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        Log.d("SlideShowView", "Memory used: " + usedMemInMB);

        WeakReference<Bitmap> bm = cachedBitmaps.get(position);
        if (bm!=null && bm.get()!=null) {
            bm.get().recycle();
            bm.clear();
        }
        cachedBitmaps.remove(position);

        Log.d("SlideShowView", "Cache now has this many bitmaps: " + cachedBitmaps.size());

        usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        Log.d("SlideShowView", "Memory used: " + usedMemInMB);
    }

    @Override
    public SlideStatus getSlideStatus(int position) {
        WeakReference<Bitmap> bm = cachedBitmaps.get(position);
        return bm!=null && bm.get()!=null && !bm.get().isRecycled() ? SlideStatus.READY : SlideStatus.UNAVAILABLE;
    }
}
