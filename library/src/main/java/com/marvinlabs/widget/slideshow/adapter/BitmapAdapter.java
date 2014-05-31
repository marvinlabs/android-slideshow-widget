package com.marvinlabs.widget.slideshow.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.marvinlabs.widget.slideshow.SlideShowAdapter;

import java.lang.ref.WeakReference;

/**
 * A base class to help handling the tasks particular to loading/discarding bitmaps for slides. This
 * can be a good base for subclasses that simply have to load the bitmaps the way they want to.
 * <p/>
 * This adapter will bind the bitmaps to an ImageView. This behaviour can be of course overloaded.
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 29/05/2014.
 */
public abstract class BitmapAdapter extends BaseAdapter implements SlideShowAdapter {

    // Hold information about the bitmaps that are being loaded and cached
    private static class BitmapCache {
        SlideStatus status = SlideStatus.LOADING;
        WeakReference<Bitmap> bitmap = null;
    }

    // The context in which the adapter was created
    private Context context;

    // Dictionary of bitmaps which have already been loaded
    private SparseArray<BitmapCache> cachedBitmaps;

    //==============================================================================================
    // GENERAL METHODS
    //==

    /**
     * Constructor
     *
     * @param context The context that will hold the views
     */
    public BitmapAdapter(Context context) {
        this.context = context;
        this.cachedBitmaps = new SparseArray<BitmapCache>(3);
    }

    /**
     * Get the main context
     *
     * @return The context in which the adapter was created
     */
    public Context getContext() {
        return context;
    }

    //==============================================================================================
    // BITMAP LOADING
    //==

    /**
     * Load the bitmap for the item at the given position. When the bitmap is loaded, this function
     * should either call {#onBitmapLoaded} or {#onBitmapNotAvailable}
     *
     * @param position The position of the item for which to load the bitmap
     */
    protected abstract void loadBitmap(int position);

    /**
     * This function should be called by subclasses once they have actually loaded the bitmap.
     *
     * @param position the position of the item
     * @param bitmap   the bitmap that has been loaded
     */
    protected void onBitmapLoaded(int position, Bitmap bitmap) {
        BitmapCache bc = cachedBitmaps.get(position);
        if (bc != null) {
            bc.status = bitmap == null ? SlideStatus.NOT_AVAILABLE : SlideStatus.READY;
            bc.bitmap = new WeakReference<Bitmap>(bitmap);
        }
    }

    /**
     * This function should be called by subclasses when they could not load a bitmap
     *
     * @param position the position of the item
     */
    protected void onBitmapNotAvailable(int position) {
        BitmapCache bc = cachedBitmaps.get(position);
        if (bc != null) {
            bc.status = SlideStatus.NOT_AVAILABLE;
            bc.bitmap = null;
        }
    }

    //==============================================================================================
    // INTERFACE IMPLEMENTATION: Adapter
    //==

    /**
     * Create the ImageView that will be used to show the bitmap.
     *
     * @return A new ImageView instance
     */
    protected ImageView newImageViewInstance() {
        ImageView iv = new ImageView(context);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // SlideShowView is a subclass of RelativeLayout. Set the layout parameters accordingly
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        iv.setLayoutParams(lp);

        return iv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv;

        if (convertView == null) {
            iv = newImageViewInstance();
        } else {
            iv = (ImageView) convertView;
        }

        BitmapCache bc = cachedBitmaps.get(position);
        if (bc == null) {
            prepareSlide(position);
            bc = cachedBitmaps.get(position);
        }

        if (bc != null && bc.status == SlideStatus.READY) {
            iv.setImageBitmap(bc.bitmap.get());
        }

        return iv;
    }

    //==============================================================================================
    // INTERFACE IMPLEMENTATION: SlideShowAdapter
    //==

    @Override
    public void prepareSlide(int position) {
        BitmapCache bc = cachedBitmaps.get(position);
        if (bc != null && bc.bitmap != null && bc.bitmap.get() != null) {
            bc.bitmap.get().recycle();
            bc.bitmap.clear();
        }

        bc = new BitmapCache();
        cachedBitmaps.put(position, bc);

        loadBitmap(position);
    }

    @Override
    public void discardSlide(int position) {
        BitmapCache bc = cachedBitmaps.get(position);
        if (bc != null && bc.bitmap != null && bc.bitmap.get() != null) {
            bc.bitmap.get().recycle();
            bc.bitmap.clear();
        }
        cachedBitmaps.remove(position);
    }

    @Override
    public SlideStatus getSlideStatus(int position) {
        BitmapCache bc = cachedBitmaps.get(position);
        return bc != null ? bc.status : SlideStatus.NOT_AVAILABLE;
    }
}
