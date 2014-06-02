package com.marvinlabs.widget.slideshow.demo;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.marvinlabs.widget.slideshow.SlideShowAdapter;
import com.marvinlabs.widget.slideshow.SlideShowView;
import com.marvinlabs.widget.slideshow.adapter.RemoteBitmapAdapter;
import com.marvinlabs.widget.slideshow.adapter.ResourceBitmapAdapter;
import com.marvinlabs.widget.slideshow.picasso.PicassoBitmapAdapter;
import com.squareup.picasso.Picasso;

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
                "http://lorempixel.com/1280/720/sports",
                "http://lorempixel.com/1280/720/nature",
                "http://lorempixel.com/1280/720/people",
                "http://lorempixel.com/1280/720/city",
        };
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;
        adapter = new RemoteBitmapAdapter(this, Arrays.asList(slideUrls), opts);
        return adapter;
    }

    private SlideShowAdapter createPicassoAdapter() {
        Picasso.with(this).setLoggingEnabled(true);

        String[] slideUrls = new String[]{
                "http://lorempixel.com/1280/720/sports",
                "http://lorempixel.com/1280/720/nature",
                "http://lorempixel.com/1280/720/people",
                "http://lorempixel.com/1280/720/city",
        };
        adapter = new PicassoBitmapAdapter(this, Arrays.asList(slideUrls));
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
        } else if (adapter instanceof PicassoBitmapAdapter) {
            Picasso.with(this).shutdown();
        }
        super.onStop();
    }

    private void startSlideShow() {
        // Create an adapter
        slideShowView.setAdapter(createResourceAdapter());
        // slideShowView.setAdapter(createRemoteAdapter());
        // slideShowView.setAdapter(createPicassoAdapter());

        // Optional customisation follows
        // slideShowView.setTransitionFactory(new RandomTransitionFactory());
        // slideShowView.setTransitionFactory(new FlipTransitionFactory());
        // slideShowView.setPlaylist(new RandomPlayList());

        // Some listeners if needed
        slideShowView.setOnSlideShowEventListener(slideShowListener);
        slideShowView.setOnSlideClickListener(slideClickListener);

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

    private SlideShowView.OnSlideClickListener slideClickListener = new SlideShowView.OnSlideClickListener() {
        @Override
        public void onItemClick(SlideShowView parent, int position) {
            Toast.makeText(SlideShowActivity.this, "Slide clicked: " + position, Toast.LENGTH_SHORT).show();
        }
    };

    private SlideShowView.OnSlideShowEventListener slideShowListener = new SlideShowView.OnSlideShowEventListener() {
        @Override
        public void beforeSlideShown(SlideShowView parent, int position) {
            Log.d("SlideShowDemo", "OnSlideShowEventListener.beforeSlideShown: " + position);
        }

        @Override
        public void onSlideShown(SlideShowView parent, int position) {
            Log.d("SlideShowDemo", "OnSlideShowEventListener.onSlideShown: " + position);
        }

        @Override
        public void beforeSlideHidden(SlideShowView parent, int position) {
            Log.d("SlideShowDemo", "OnSlideShowEventListener.beforeSlideHidden: " + position);
        }

        @Override
        public void onSlideHidden(SlideShowView parent, int position) {
            Log.d("SlideShowDemo", "OnSlideShowEventListener.onSlideHidden: " + position);
        }
    };

}
