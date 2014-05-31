package com.marvinlabs.widget.slideshow.demo;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.marvinlabs.widget.slideshow.SlideShowAdapter;
import com.marvinlabs.widget.slideshow.SlideShowView;
import com.marvinlabs.widget.slideshow.adapter.RemoteBitmapAdapter;
import com.marvinlabs.widget.slideshow.adapter.ResourceBitmapAdapter;

import java.util.Arrays;


public class SlideShowActivity extends Activity {

    private SlideShowView slideShowView;
    private SlideShowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Go fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Activity layout
        setContentView(R.layout.activity_slideshow);

        slideShowView = (SlideShowView) findViewById(R.id.slideshow);
    }

    private SlideShowAdapter createRemoteAdapter() {
        String[] slideUrls = new String[]{
                "http://www.thisiscolossal.com/wp-content/uploads/2014/01/flickr-5.jpg",
                "http://www.thisiscolossal.com/wp-content/uploads/2014/01/flickr-8.jpg",
                "http://www.thisiscolossal.com/wp-content/uploads/2013/11/flickr-2.jpg",
                "http://www.thisiscolossal.com/wp-content/uploads/2013/11/flickr-8.jpg",
            };
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;
        adapter = new RemoteBitmapAdapter(this, Arrays.asList(slideUrls), opts);
        return adapter;
    }

    private SlideShowAdapter createResourceAdapter() {
        int[] slideResources = new int[]{R.raw.slide_01, R.raw.slide_02, R.raw.slide_03, R.raw.slide_04};
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;
        adapter = new ResourceBitmapAdapter(this, slideResources, opts);
        return adapter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSlideShow();
    }

    @Override
    protected void onStop() {
        if (adapter instanceof RemoteBitmapAdapter) {
            ((RemoteBitmapAdapter) adapter).stopAllDownloads();
        }
        super.onStop();
    }

    private void startSlideShow() {
        // Create an adapter
        // slideShowView.setAdapter(createResourceAdapter());
        slideShowView.setAdapter(createRemoteAdapter());

        // Optional customisation follows
        // slideShowView.setTransitionFactory(new SlideAndZoomTransitionFactory());

        // Then attach the adapter
        slideShowView.play();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.slideshow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
