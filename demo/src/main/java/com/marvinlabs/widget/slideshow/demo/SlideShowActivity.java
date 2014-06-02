package com.marvinlabs.widget.slideshow.demo;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.marvinlabs.widget.slideshow.PlayList;
import com.marvinlabs.widget.slideshow.SlideShowAdapter;
import com.marvinlabs.widget.slideshow.SlideShowView;
import com.marvinlabs.widget.slideshow.adapter.RemoteBitmapAdapter;
import com.marvinlabs.widget.slideshow.adapter.ResourceBitmapAdapter;
import com.marvinlabs.widget.slideshow.playlist.RandomPlayList;
import com.marvinlabs.widget.slideshow.playlist.SequentialPlayList;
import com.marvinlabs.widget.slideshow.transition.FadeTransitionFactory;
import com.marvinlabs.widget.slideshow.transition.NoTransitionFactory;
import com.marvinlabs.widget.slideshow.transition.SlideAndZoomTransitionFactory;

import java.util.Arrays;


public class SlideShowActivity extends Activity {

    private SlideShowView slideShowView;
    private SlideShowAdapter adapter;
    private TextView currentSlideTextView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private static final int PLAYLIST_SEQUENTIAL = 0;
    private static final int PLAYLIST_RANDOM = 1;

    private static int playlistType = PLAYLIST_SEQUENTIAL;

    private static final int TRANSITION_NONE = 0;
    private static final int TRANSITION_FADE = 1;
    private static final int TRANSITION_SLIDE_AND_ZOOM = 2;

    private static int transitionType = TRANSITION_FADE;

    private Spinner durationSpinner;
    private Spinner playlistSpinner;
    private Spinner transitionSpinner;

    private long duration = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Activity layout
        setContentView(R.layout.activity_slideshow);

        slideShowView = (SlideShowView) findViewById(R.id.slideshow);

        currentSlideTextView = (TextView) findViewById(R.id.slide_text);

        drawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);


        if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            drawerLayout.setScrimColor(Color.TRANSPARENT);
        } else {

            drawerToggle = new ActionBarDrawerToggle(this,
                    drawerLayout,
                    R.drawable.ic_drawer,
                    R.string.app_name,
                    R.string.app_name);

            drawerLayout.setDrawerListener(drawerToggle);
        }

        /*
         * Duration settings
         */

        durationSpinner = (Spinner) findViewById(R.id.slide_duration_spinner);
        // This prevents the initial OnItemSelectedListener from starting
        durationSpinner.setSelection(0, false);
        durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                duration = getResources().getIntArray(R.array.duration_array)[i];
                setPlaylist();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*
         * Playlist type settings
         */

        playlistSpinner = (Spinner) findViewById(R.id.playlists_spinner);
        playlistSpinner.setSelection(0, false);
        playlistSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                playlistType = i;
                setPlaylist();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*
         * Transition settings
         */

        transitionSpinner = (Spinner) findViewById(R.id.transitions_spinner);
        transitionSpinner.setSelection(transitionType, false);
        transitionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                transitionType = i;
                switch(transitionType) {
                    case TRANSITION_NONE:
                        slideShowView.setTransitionFactory(new NoTransitionFactory());
                        break;
                    case TRANSITION_FADE:
                        slideShowView.setTransitionFactory(new FadeTransitionFactory(duration));
                        break;
                    case TRANSITION_SLIDE_AND_ZOOM:
                        slideShowView.setTransitionFactory(new SlideAndZoomTransitionFactory(duration));
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    private void setPlaylist() {
        slideShowView.stop();
        PlayList playList = null;

        switch (playlistType) {
            case PLAYLIST_SEQUENTIAL:
                playList = new SequentialPlayList();
                ((SequentialPlayList) playList).setSlideDuration(duration);
                break;
            case PLAYLIST_RANDOM:
                playList = new RandomPlayList();
                ((RandomPlayList) playList).setSlideDuration(duration);
                break;
        }


        // This makes the library crash
        slideShowView.setPlaylist(playList);
        slideShowView.play();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(drawerToggle != null) {
            drawerToggle.syncState();
        }
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
        // slideShowView.setTransitionFactory(new SlideAndZoomTransitionFactory(2000));
        slideShowView.setPlaylist(new RandomPlayList());

        // Some listeners if needed
        slideShowView.setOnSlideShowEventListener(slideShowListener);
        slideShowView.setOnSlideClickListener(slideClickListener);

        // Then attach the adapter
        slideShowView.play();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
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
            currentSlideTextView.setText(String.format(getString(R.string.current_slide), position));
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
