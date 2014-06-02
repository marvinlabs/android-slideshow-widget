package com.marvinlabs.widget.slideshow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.marvinlabs.widget.slideshow.playlist.RandomPlayList;
import com.marvinlabs.widget.slideshow.playlist.SequentialPlayList;
import com.marvinlabs.widget.slideshow.transition.FadeTransitionFactory;
import com.marvinlabs.widget.slideshow.transition.FlipTransitionFactory;
import com.marvinlabs.widget.slideshow.transition.NoTransitionFactory;
import com.marvinlabs.widget.slideshow.transition.RandomTransitionFactory;
import com.marvinlabs.widget.slideshow.transition.SlideAndZoomTransitionFactory;
import com.marvinlabs.widget.slideshow.transition.ZoomTransitionFactory;

import static com.marvinlabs.widget.slideshow.SlideShowAdapter.SlideStatus;

/**
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 28/05/2014.
 */
public class SlideShowView extends RelativeLayout implements View.OnClickListener {

    private enum Status {
        STOPPED, PAUSED, PLAYING;
    }

    // The handler that will handle our timing job
    private Handler slideHandler;

    // The progress view shown when slides are loading
    private View progressIndicator;

    // Whether or not the slideShow is currently playing
    private Status status = Status.STOPPED;

    // The playlist
    private PlayList playlist = null;

    // The adapter
    private SlideShowAdapter adapter = null;

    // The transition maker between slides
    private TransitionFactory transitionFactory = null;

    // The number of slides we have automatically skipped
    private int notAvailableSlidesSkipped = 0;

    // The slide show listener
    private OnSlideShowEventListener slideShowEventListener;

    // The item click listener
    private OnSlideClickListener slideClickListener;

    // A selector to show when the view is clicked
    private StateListDrawable onClickedDrawable;

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

    // Recycled views for the adapter to reuse
    SparseArray<View> recycledViews;

    // Wait for the current slide to finish loading
    private Runnable waitForCurrentSlide = new Runnable() {
        @Override
        public void run() {
            final PlayList pl = getPlaylist();
            final SlideStatus status = adapter.getSlideStatus(pl.getCurrentSlide());

            switch (status) {
                case LOADING:
                    slideHandler.postDelayed(this, 100);
                    break;

                default:
                    playCurrentSlide();
            }
        }
    };

    // Simply show the next slide (for use with slideHandler.postDelayed)
    private Runnable moveToNextSlide = new Runnable() {
        @Override
        public void run() {
            next();
        }
    };

    //==============================================================================================
    // GENERAL METHODS
    //==

    public SlideShowView(Context context) {
        this(context, null, 0);
    }

    public SlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialise();
        readAttributeSet(context, attrs, defStyle);
    }

    private void readAttributeSet(Context context, AttributeSet attrs, int defStyle) {
        if (attrs == null) return;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlideShowView);

        // Clicked drawable
        try {
            onClickedDrawable = (StateListDrawable) a.getDrawable(R.styleable.SlideShowView_selector);
        } catch (Exception e) { /* ignored */ }

        if (onClickedDrawable == null) {
            TypedArray themeAttrs = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.selectableItemBackground});
            onClickedDrawable = (StateListDrawable) themeAttrs.getDrawable(0);
        }

        // Playlist stuff
        int playlistType = a.getInteger(R.styleable.SlideShowView_playlist, 1);
        long slideDuration = a.getInteger(R.styleable.SlideShowView_slideDuration, (int) SequentialPlayList.DEFAULT_SLIDE_DURATION);
        boolean loop = a.getBoolean(R.styleable.SlideShowView_loop, true);

        switch (playlistType) {
            case 2: {
                RandomPlayList pl = new RandomPlayList();
                pl.setLooping(loop);
                pl.setSlideDuration(slideDuration);
                setPlaylist(pl);
                break;
            }
            case 1:
            default: {
                SequentialPlayList pl = new SequentialPlayList();
                pl.setLooping(loop);
                pl.setSlideDuration(slideDuration);
                setPlaylist(pl);
            }
        }

        // Transition stuff
        int transitionType = a.getInteger(R.styleable.SlideShowView_transition, 3);
        long transitionDuration = a.getInteger(R.styleable.SlideShowView_transitionDuration, (int) FadeTransitionFactory.DEFAULT_DURATION);

        switch (transitionType) {
            case 1: {
                NoTransitionFactory tf = new NoTransitionFactory();
                setTransitionFactory(tf);
                break;
            }
            case 2: {
                RandomTransitionFactory tf = new RandomTransitionFactory(transitionDuration);
                setTransitionFactory(tf);
                break;
            }
            case 4: {
                ZoomTransitionFactory tf = new ZoomTransitionFactory(transitionDuration);
                setTransitionFactory(tf);
                break;
            }
            case 5: {
                SlideAndZoomTransitionFactory tf = new SlideAndZoomTransitionFactory(transitionDuration);
                setTransitionFactory(tf);
                break;
            }
            case 6: {
                FlipTransitionFactory tf = new FlipTransitionFactory(transitionDuration);
                tf.setDirection(FlipTransitionFactory.FlipAxis.HORIZONTAL);
                setTransitionFactory(tf);
                break;
            }
            case 7: {
                FlipTransitionFactory tf = new FlipTransitionFactory(transitionDuration);
                tf.setDirection(FlipTransitionFactory.FlipAxis.VERTICAL);
                setTransitionFactory(tf);
                break;
            }
            case 3:
            default: {
                FadeTransitionFactory tf = new FadeTransitionFactory(transitionDuration);
                setTransitionFactory(tf);
            }
        }

        // Don't forget to release some memory
        a.recycle();
    }

    private void initialise() {
        slideHandler = new Handler();
        recycledViews = new SparseArray<View>();
    }


    //==============================================================================================
    // VIEW LIFECYCLE METHODS
    //==

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        slideHandler.removeCallbacksAndMessages(null);
        if (this.adapter != null) {
            this.adapter.unregisterDataSetObserver(adapterObserver);
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onFinishInflate() {
        // Check if we have a progress indicator as a child, if not, create one
        progressIndicator = findViewById(R.id.progress_indicator);
        if (progressIndicator == null) {
            ProgressBar pb = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
            pb.setIndeterminate(true);
            LayoutParams lp = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            pb.setLayoutParams(lp);

            progressIndicator = pb;
        } else {
            removeView(progressIndicator);
        }

        super.onFinishInflate();
    }

    /**
     * Make sure the view is configured to play the slideShow. If not, we will provide some default components when we can
     */
    private void ensureComponentsAvailable() {
        if (adapter == null) {
            throw new RuntimeException("The SlideShowView needs an adapter (currently null)");
        }
    }

    /*
     * When the size of the view changes, the size of the selector must scale with it
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (onClickedDrawable != null) {
            onClickedDrawable.setBounds(0, 0, w, h);
        }
    }

    /*
     * Draw on top of the view after all its children have been drawn
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (onClickedDrawable != null) {
            onClickedDrawable.draw(canvas);
        }
    }

    /*
     * In order to show the selector, its drawablestate must be the same as the view's one
     */
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (onClickedDrawable != null) {
            onClickedDrawable.setState(getDrawableState());
            invalidate();
        }
    }

    //==============================================================================================
    // TOUCH HANDLING
    //==

    @Override
    public void onClick(View view) {
        notifySlideClicked();
    }

    //==============================================================================================
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

            prepareSlide(pl.getFirstSlide());
        }
    }

    //==============================================================================================
    // ANIMATION-RELATED METHODS
    //==

    public TransitionFactory getTransitionFactory() {
        if (transitionFactory == null) {
            transitionFactory = new FadeTransitionFactory();
        }
        return transitionFactory;
    }

    public void setTransitionFactory(TransitionFactory transitionFactory) {
        this.transitionFactory = transitionFactory;
    }

    //==============================================================================================
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
        getPlaylist().next();
        playCurrentSlide();
    }

    /**
     * Stop playing the show
     */
    public void stop() {
        status = Status.STOPPED;

        // Remove all callbacks
        slideHandler.removeCallbacksAndMessages(null);

        // TODO Use the out transition to hide the current view
        // Hide all visible views
        removeAllViews();
    }

    /**
     * Play the current slide in the playlist if it is ready. If that slide is not available, we
     * move to the next one. If that slide is loading, we wait until it is ready and then we play
     * it.
     */
    protected void playCurrentSlide() {
        final PlayList pl = getPlaylist();
        final int position = pl.getCurrentSlide();
        final SlideStatus slideStatus = adapter.getSlideStatus(position);

        // Don't play anything if we have reached the end
        if (position < 0) {
            stop();
            return;
        }

        // Stop anything planned
        slideHandler.removeCallbacksAndMessages(null);

        // If the slide is ready, then we can display it straight away
        switch (slideStatus) {
            case READY:
                notAvailableSlidesSkipped = 0;

                // We are playing the slide show!
                status = Status.PLAYING;

                // Schedule next slide
                slideHandler.postDelayed(moveToNextSlide, pl.getSlideDuration(position));

                // Display the slide
                displayCurrentSlide();

                break;

            case NOT_AVAILABLE:
                Log.w("SlideShowView", "Slide is not available: " + position);

                // Stop if we have already skipped all slides
                ++notAvailableSlidesSkipped;
                if (notAvailableSlidesSkipped < adapter.getCount()) {
                    prepareSlide(pl.getNextSlide());
                    next();
                } else {
                    Log.w("SlideShowView", "Skipped too many slides in a row. Stopping playback.");
                    stop();
                }
                break;

            case LOADING:
                Log.d("SlideShowView", "Slide is not yet ready, waiting for it: " + position);

                // Show an indicator to the user
                showProgressIndicator();

                // Start waiting for the slide to be available
                slideHandler.post(waitForCurrentSlide);

                break;
        }

        // Slide is loading, we show the progress indicator and wait for it before making the
        // transition

    }

    /**
     * Prepare the given slide for playback. Basically a safe wrapper around
     * SlideShowAdapter#prepareSlide
     *
     * @param position The index of the slide to prepare
     */
    private void prepareSlide(int position) {
        if (adapter != null && position >= 0) {
            adapter.prepareSlide(position);
        }
    }

    //==============================================================================================
    // VIEW HANDLING METHODS
    //==

    /**
     * Display the view for the given slide, launching the appropriate transitions if available
     */
    private void displayCurrentSlide() {
        final PlayList pl = getPlaylist();
        final int currentPosition = pl.getCurrentSlide();
        final int previousPosition = pl.getPreviousSlide();

        Log.v("SlideShowView", "Displaying slide at position: " + currentPosition);

        // Hide the progress indicator
        hideProgressIndicator();

        // Add the slide view to our hierarchy
        final View inView = getSlideView(currentPosition);
        inView.setVisibility(View.INVISIBLE);
        if(inView.getParent() != null) {
            removeView(inView);
        }
        addView(inView);

        // Notify that the slide is about to be shown
        notifyBeforeSlideShown(currentPosition);

        // Transition between current and new slide
        final TransitionFactory tf = getTransitionFactory();

        final Animator inAnimator = tf.getInAnimator(inView, this, previousPosition, currentPosition);
        if (inAnimator != null) {
            inAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    inView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    notifySlideShown(currentPosition);
                }
            });
            inAnimator.start();
        } else {
            inView.setVisibility(View.VISIBLE);
            notifySlideShown(currentPosition);
        }

        int childCount = getChildCount();
        if (childCount > 1) {
            notifyBeforeSlideHidden(previousPosition);

            final View outView = getChildAt(0);
            final Animator outAnimator = tf.getOutAnimator(outView, this, previousPosition, currentPosition);
            if (outAnimator != null) {
                outAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        outView.setVisibility(View.INVISIBLE);
                        notifySlideHidden(previousPosition);
                        recyclePreviousSlideView(previousPosition, outView);
                    }
                });
                outAnimator.start();
            } else {
                outView.setVisibility(View.INVISIBLE);
                notifySlideHidden(previousPosition);
                recyclePreviousSlideView(previousPosition, outView);
            }
        }
    }

    /**
     * Get a view for the slide at the given index. If possible, we will reuse a view from our
     * recycled pool. If not, we will ask the adapter to create one from scratch.
     *
     * @param position The index of the slide to get a view for
     * @return The view (either a new one or a recycled one with updated properties
     */
    private View getSlideView(int position) {
        // Do we have a view in our recycling bean?
        int viewType = adapter.getItemViewType(position);
        View recycledView = recycledViews.get(viewType);

        View v = adapter.getView(position, recycledView, this);
        return v;
    }

    /**
     * Once the previous slide has disappeared, we remove its view from our hierarchy and add it to
     * the views that can be re-used.
     *
     * @param position The position of the slide to recycle
     * @param view     The view to recycle
     */
    private void recyclePreviousSlideView(int position, View view) {
        // Remove view from our hierarchy
        removeView(view);

        // Add to recycled views
        int viewType = adapter.getItemViewType(position);
        recycledViews.put(viewType, view);
        view.destroyDrawingCache();

        if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(null);
        }

        Log.d("SlideShowView", "View added to recycling bin: " + view);

        // The adapter can recycle some memory with discard slide
        adapter.discardSlide(position);

        // The adapter can prepare the next slide
        prepareSlide(getPlaylist().getNextSlide());
    }

    //==============================================================================================
    // PROGRESS METHODS
    //==

    /**
     * Show the progress indicator when a slide is being loaded
     */
    protected void showProgressIndicator() {
        removeView(progressIndicator);

        progressIndicator.setAlpha(0);
        addView(progressIndicator);
        progressIndicator.animate().alpha(1).setDuration(500).start();
    }

    /**
     * Hide the progress indicator once the slide has finished loading
     */
    protected void hideProgressIndicator() {
        removeView(progressIndicator);
    }

    //==============================================================================================
    // SLIDESHOW LISTENERS
    //==

    /**
     * Interface to be implemented to listen to the slide show events (slide changing, ...)
     */
    public interface OnSlideShowEventListener {

        /**
         * Called before the slide is actually shown, that is before we start the IN transition
         *
         * @param parent   The parent SlideShowView
         * @param position The position of the slide that is about to be displayed
         */
        public void beforeSlideShown(SlideShowView parent, int position);

        /**
         * Called once the slide is actually shown, that is after the IN transition is complete
         *
         * @param parent   The parent SlideShowView
         * @param position The position of the slide that is displayed
         */
        public void onSlideShown(SlideShowView parent, int position);

        /**
         * Called before the slide is actually hidden, that is before we start the OUT transition
         *
         * @param parent   The parent SlideShowView
         * @param position The position of the slide that is hidden
         */
        public void beforeSlideHidden(SlideShowView parent, int position);

        /**
         * Called once the slide is actually hidden, that is after the OUT transition is complete
         *
         * @param parent   The parent SlideShowView
         * @param position The position of the slide that is about to be hidden
         */
        public void onSlideHidden(SlideShowView parent, int position);
    }

    /**
     * Get the current slide show listener
     *
     * @return The current listener (null if none)
     */
    public OnSlideShowEventListener getOnSlideShowEventListener() {
        return slideShowEventListener;
    }

    /**
     * Set the slide show listener
     *
     * @param slideShowEventListener the slide show listener (null if you want to remove the current one)
     */
    public void setOnSlideShowEventListener(OnSlideShowEventListener slideShowEventListener) {
        this.slideShowEventListener = slideShowEventListener;
    }

    /**
     * Notify the listeners that a slide got shown
     */
    private void notifySlideShown(int position) {
        if (slideShowEventListener != null) {
            slideShowEventListener.onSlideShown(this, position);
        }
    }

    /**
     * Notify the listeners that a slide got shown
     */
    private void notifySlideHidden(int position) {
        if (slideShowEventListener != null) {
            slideShowEventListener.onSlideHidden(this, position);
        }
    }

    /**
     * Notify the listeners that a slide got shown
     */
    private void notifyBeforeSlideShown(int position) {
        if (slideShowEventListener != null) {
            slideShowEventListener.beforeSlideShown(this, position);
        }
    }

    /**
     * Notify the listeners that a slide got shown
     */
    private void notifyBeforeSlideHidden(int position) {
        if (slideShowEventListener != null) {
            slideShowEventListener.beforeSlideHidden(this, position);
        }
    }

    //==============================================================================================
    // SLIDE CLICKED METHODS
    //==

    /**
     * Interface to be implemented to get notified when a slide gets clicked/tapped
     */
    public interface OnSlideClickListener {

        /**
         * Callback called when the slide is clicked
         *
         * @param parent   The parent SlideShowView
         * @param position The position of the slide that got clicked
         */
        public void onItemClick(SlideShowView parent, int position);
    }

    /**
     * Get the current slide click listener
     *
     * @return The current listener (null if none)
     */
    public OnSlideClickListener getOnSlideClickListener() {
        return slideClickListener;
    }

    /**
     * Set the click listener for the slides and makes this view clickable
     *
     * @param slideClickListener the click listener (null if you want to remove the current one)
     */
    public void setOnSlideClickListener(OnSlideClickListener slideClickListener) {
        this.slideClickListener = slideClickListener;
        if (slideClickListener != null) {
            setClickable(true);
            setOnClickListener(this);
        } else {
            setClickable(false);
            setOnClickListener(null);
        }
    }

    /**
     * Notify the listeners that a slide got clicked
     */
    private void notifySlideClicked() {
        if (slideClickListener != null) {
            slideClickListener.onItemClick(this, getPlaylist().getCurrentSlide());
        }
    }
}
