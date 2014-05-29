package com.marvinlabs.widget.slideshow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.marvinlabs.widget.slideshow.playlist.SequentialPlayList;
import com.marvinlabs.widget.slideshow.transition.FadeTransitionFactory;
import com.marvinlabs.widget.slideshow.transition.NoTransitionFactory;

/**
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 28/05/2014.
 */
public class SlideShowView extends FrameLayout {

    private enum Status {
        STOPPED, PAUSED, PLAYING;
    }

    // The handle that will handle our timing job
    private Handler slideHandler;

    // Whether or not the slideShow is currently playing
    private Status status = Status.STOPPED;

    // The playlist
    private PlayList playlist = null;

    // The adapter
    private SlideShowAdapter adapter = null;

    // The transition maker between slides
    private SlideTransitionFactory transitionFactory = null;

    // Watch the adapter data
    private DataSetObserver adapterObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            if (adapter != null) {
                PlayList pl = getPlaylist();
                pl.onSlideCountChanged(adapter.getCount());
            }
        }

        @Override
        public void onInvalidated() {
        }
    };

    // Recycled views
    SparseArray<View> recycledViews;

//    // A thread in charge of waiting for the current slide to be available before displaying it
//    private Thread slideWaiterThread = null;
//
//    // Wait until next slide is available and display it
//    // This runnable should be ran on a background thread!
//    private Runnable showNextSlideWhenAvailable = new Runnable() {
//        @Override
//        public void run() {
//            int slideIndex = playlist.getNextSlide();
//            if (slideIndex < 0) {
//                stop();
//                return;
//            }
//
//            SlideShowAdapter.SlideStatus status;
//            while ((status = adapter.getSlideStatus(slideIndex)) == SlideShowAdapter.SlideStatus.LOADING) {
//                try {
//                    Thread.sleep(20);
//                } catch (InterruptedException e) { /* Ignored */ }
//            }
//
//            // If slide is available, show it, else skip it and move to next one
//            if (status == SlideShowAdapter.SlideStatus.READY) {
//                playlist.next();
//                slideHandler.removeCallbacks(displayCurrentSlide);
//                slideHandler.post(displayCurrentSlide);
//            } else {
//                next();
//            }
//        }
//    };
//
//    // Display the next slide. This should be ran on the UI thread.
//    private Runnable displayCurrentSlide = new Runnable() {
//        @Override
//        public void run() {
//            displaySlide(playlist.getCurrentSlide());
//        }
//    };

    // Simply show the next slide (for use with slideHandle.postDelayed)
    private Runnable moveToNextSlide = new Runnable() {
        @Override
        public void run() {
            Log.d("SlideShowView", "Automatically moving to next slide");
            next();
        }
    };

    // VIEW LIFECYCLE METHODS
    //==

    public SlideShowView(Context context) {
        super(context);
        initialise();
    }

    public SlideShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise();
    }

    public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialise();
    }

    private void initialise() {
        slideHandler = new Handler();
        recycledViews = new SparseArray<View>();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (this.adapter != null) {
            this.adapter.unregisterDataSetObserver(adapterObserver);
        }
        super.onDetachedFromWindow();
    }

    /**
     * Make sure the view is configured to play the slideShow. If not, we will provide some default components when we can
     */
    private void ensureComponentsAvailable() {
        if (adapter == null) {
            throw new RuntimeException("The SlideShowView needs an adapter (currently null)");
        }
    }

    // DATA HANDLING METHODS
    //==

    /**
     * Get the adapter between slide data and slide views
     *
     * @return the SlideShowAdapter
     */
    public SlideShowAdapter getAdapter() {
        return adapter;
    }

    /**
     * Set the adapter that will create views for the slides
     *
     * @param adapter
     */
    public void setAdapter(SlideShowAdapter adapter) {
        if (this.adapter != null) {
            this.adapter.unregisterDataSetObserver(adapterObserver);
        }
        this.adapter = adapter;
        this.adapter.registerDataSetObserver(adapterObserver);

        if (adapter != null) {
            PlayList pl = getPlaylist();
            pl.onSlideCountChanged(adapter.getCount());
            pl.rewind();
        }
    }

    // ANIMATION-RELATED METHODS
    //==

    public SlideTransitionFactory getTransitionFactory() {
        if (transitionFactory == null) {
            transitionFactory = new FadeTransitionFactory();
        }
        return transitionFactory;
    }

    public void setTransitionFactory(SlideTransitionFactory transitionFactory) {
        this.transitionFactory = transitionFactory;
    }

    // SLIDESHOW CONTROL METHODS
    //==

    /**
     * Get the playlist. This accessor lazy loads a default playlist if none is already set
     *
     * @return
     */
    public PlayList getPlaylist() {
        if (playlist == null) {
            setPlaylist(new SequentialPlayList());
        }
        return playlist;
    }

    /**
     * Set the playlist
     *
     * @param playlist The playlist to set
     */
    public void setPlaylist(PlayList playlist) {
        this.playlist = playlist;
        if (adapter != null) {
            playlist.onSlideCountChanged(adapter.getCount());
        }
    }

    /**
     * Start playing the show
     */
    public void play() {
        ensureComponentsAvailable();

        // Display the next slide in show
        getPlaylist().rewind();
        next();
    }

    /**
     * Move to the next slide
     */
    public void next() {
        final PlayList pl = getPlaylist();
        int currentSlide = pl.next();
        if (currentSlide < 0) {
            stop();
            return;
        }

        displaySlide(currentSlide);

        // Schedule next slide
        slideHandler.removeCallbacks(moveToNextSlide);
        slideHandler.postDelayed(moveToNextSlide, pl.getSlideDuration(currentSlide));

        // We are playing the slide show!
        status = Status.PLAYING;
    }

    /**
     * Stop playing the show
     */
    public void stop() {
        status = Status.STOPPED;

        // Remove all callbacks
        slideHandler.removeCallbacksAndMessages(null);

        // Hide all visible views
        removeAllViews();
    }

    // VIEW HANDLING METHODS
    //==

    /**
     * Display the view for the given slide, launching the appropriate transitions if available
     *
     * @param position
     */
    private void displaySlide(int position) {
        // Ignore invalid positions
        if (position < 0 || position >= adapter.getCount()) {
            Log.w("SlideShowView", "Requesting to show an invalid slide at position: " + position);
            return;
        }

        Log.d("SlideShowView", "-----------------------------------");
        Log.d("SlideShowView", "Displaying slide at position: " + position);

        // Add the slide view to our hierarchy
        final View inView = getSlideView(position);
        inView.setVisibility(View.INVISIBLE);
        addView(inView);

        // Transition between current and new slide
        final SlideTransitionFactory tf = getTransitionFactory();
        final int previousSlidePosition = playlist.getPreviousSlide();

        final ViewPropertyAnimator inAnimator = tf.getInAnimator(inView, this, previousSlidePosition, position);
        if (inAnimator != null) {
            inAnimator.setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    inView.setVisibility(View.VISIBLE);
                }
            }).start();
        } else {
            inView.setVisibility(View.VISIBLE);
        }

        int childCount = getChildCount();
        if (childCount > 1) {
            final View outView = getChildAt(0);
            final ViewPropertyAnimator outAnimator = tf.getOutAnimator(outView, this, previousSlidePosition, position);
            if (outAnimator != null) {
                outAnimator.setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        outView.setVisibility(View.INVISIBLE);
                        recyclePreviousSlideView(previousSlidePosition, outView);
                    }
                }).start();
            } else {
                outView.setVisibility(View.INVISIBLE);
                recyclePreviousSlideView(previousSlidePosition, outView);
            }
        }
    }

    private View getSlideView(int position) {
        // Do we have a view in our recycling bean?
        int viewType = adapter.getItemViewType(position);
        View recycledView = recycledViews.get(viewType);

        View v = adapter.getView(position, recycledView, this);
        return v;
    }

    private void recyclePreviousSlideView(int position, View view) {
        // Remove view from our hierarchy
        removeView(view);

        // Add to recycled views
        int viewType = adapter.getItemViewType(position);
        recycledViews.put(viewType, view);
        view.destroyDrawingCache();

        if (view instanceof ImageView) {
            ((ImageView)view).setImageDrawable(null);
        }

        Log.d("SlideShowView", "View added to recycling bin: " + view);

        // The adapter can recycle some memory with discard slide
        adapter.discardSlide(position);

        // The adapter can prepare the next slide
        final int nextSlideIndex = getPlaylist().getNextSlide();
        if (nextSlideIndex >= 0) {
            adapter.prepareSlide(nextSlideIndex);
        }
    }


}
