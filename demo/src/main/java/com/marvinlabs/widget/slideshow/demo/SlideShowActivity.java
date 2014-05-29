package com.marvinlabs.widget.slideshow.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.marvinlabs.widget.slideshow.SlideShowView;
import com.marvinlabs.widget.slideshow.adapter.SimpleImageResourceAdapter;


public class SlideShowActivity extends Activity {

    private SlideShowView slideShowView;

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

    @Override
    protected void onResume() {
        super.onResume();
        startSlideShow();
    }

    private void startSlideShow() {
        // A simple image slide show
        int[] slideResources = new int[]{R.raw.slide_01, R.raw.slide_02, R.raw.slide_03, R.raw.slide_04};
        slideShowView.setAdapter(new SimpleImageResourceAdapter(this, slideResources));
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
