package com.marvinlabs.widget.slideshow.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.common.collect.Lists;
import com.marvinlabs.widget.slideshow.SlideShowAdapter;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * Created by vprat on 29/05/2014.
 */
public class RemoteImageResourceAdapter extends BaseAdapter implements SlideShowAdapter {

    // The context
    private Context context;

    // URLs of the images to load
    private List<String> slideUrls;

    // A dictionary of drawables for the slides we have loaded
    private SparseArray<CachedSlide> cachedSlides;

    public RemoteImageResourceAdapter(Context context, Collection<String> slideUrls) {
        this.context = context;
        this.slideUrls = Lists.newArrayList(slideUrls);
        this.cachedSlides = new SparseArray<CachedSlide>(3);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv;

        if (convertView == null) {
            iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            iv = (ImageView) convertView;
        }

        CachedSlide cs = cachedSlides.get(position);
        if (cs != null && cs.status == SlideStatus.READY) {
            iv.setImageBitmap(cs.bitmap);
        } else {
            iv.setImageResource(0);
        }

        return iv;
    }

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

    @Override
    public void prepareSlide(int position) {
        CachedSlide cs = new CachedSlide();
        cachedSlides.put(position, cs);


    }

    @Override
    public void discardSlide(int position) {
        CachedSlide cs = cachedSlides.get(position);
        if (cs != null) {
            if (cs.task!=null) {
                cs.task.cancel(true);
                cs.task = null;
            }
            if (cs.bitmap != null) {
                cs.bitmap.recycle();
                cs.bitmap = null;
            }
        }
        cachedSlides.delete(position);
    }

    @Override
    public SlideStatus getSlideStatus(int position) {
        return SlideStatus.READY;
    }

    /**
     * Holds information about the bitmap and the status
     */
    private static class CachedSlide {
        SlideStatus status = SlideStatus.LOADING;
        Bitmap bitmap = null;
        DownloadImageTask task = null;
    }

    /**
     * A task that downloads an image from the Internet and loads it into a bitmap
     */
    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        private CachedSlide cachedSlide;

        public DownloadImageTask(CachedSlide cachedSlide) {
            this.cachedSlide = cachedSlide;
            this.cachedSlide.task = this;
        }

        protected Bitmap doInBackground(String... urls) {
            InputStream in = null;
            try {
                in = new java.net.URL(urls[0]).openStream();
                Bitmap bm = BitmapFactory.decodeStream(in);

                if (isCancelled()) {
                    if (bm != null) bm.recycle();
                    return null;
                }

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

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                cachedSlide.bitmap = result;
                cachedSlide.status = SlideStatus.READY;
                cachedSlide.task = null;
            } else {
                cachedSlide.status = SlideStatus.UNAVAILABLE;
            }
        }
    }

}
