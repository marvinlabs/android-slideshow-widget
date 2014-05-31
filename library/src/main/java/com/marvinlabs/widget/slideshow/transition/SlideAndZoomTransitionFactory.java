package com.marvinlabs.widget.slideshow.transition;

import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.marvinlabs.widget.slideshow.SlideShowView;
import com.marvinlabs.widget.slideshow.SlideTransitionFactory;

/**
 * A transition maker to fade the slides in and out
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 28/05/2014.
 */
public class SlideAndZoomTransitionFactory implements SlideTransitionFactory {

    private static final long DEFAULT_DURATION = 500;

    // Duration for the transition in ms
    private long duration = DEFAULT_DURATION;

    // Interpolator to use for the transition
    private Interpolator interpolator;

    //==============================================================================================
    // GENERAL METHODS
    //==

    /**
     * Default constuctor
     */
    public SlideAndZoomTransitionFactory() {
        this(DEFAULT_DURATION);
    }

    /**
     * Constructor
     *
     * @param duration Duration for the transition in ms
     */
    public SlideAndZoomTransitionFactory(long duration) {
        this.duration = duration;
        this.interpolator = new AccelerateDecelerateInterpolator();
    }

    /**
     * Get the transition duration
     *
     * @return duration in ms
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Set the transition duration
     *
     * @param duration duration in ms
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Get the interpolator used for the transitions
     *
     * @return the interpolator
     */
    public Interpolator getInterpolator() {
        return interpolator;
    }

    /**
     * Set the interpolator used for the transitions
     *
     * @param interpolator the interpolator
     */
    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    //==============================================================================================
    // INTERFACE IMPLEMENTATION: SlideTransitionFactory
    //==

    @Override
    public ViewPropertyAnimator getInAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        target.setAlpha(0);
        target.setScaleX(0);
        target.setScaleY(0);
        target.setTranslationX(0);
        return target.animate().setDuration(getDuration()).setInterpolator(interpolator).alpha(1).scaleX(1).scaleY(1);
    }

    @Override
    public ViewPropertyAnimator getOutAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        target.setAlpha(1);
        return target.animate().setDuration(getDuration()).setInterpolator(interpolator).alpha(0).translationX(-parent.getWidth());
    }

}
