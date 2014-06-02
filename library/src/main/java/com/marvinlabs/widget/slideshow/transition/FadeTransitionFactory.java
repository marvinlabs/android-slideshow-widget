package com.marvinlabs.widget.slideshow.transition;

import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

import com.marvinlabs.widget.slideshow.SlideShowView;

/**
 * A transition maker to fade the slides in and out
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 28/05/2014.
 */
public class FadeTransitionFactory extends BaseTransitionFactory {

    //==============================================================================================
    // GENERAL METHODS
    //==

    /**
     * Default constuctor
     */
    public FadeTransitionFactory() {
        super();
    }

    /**
     * Constructor
     *
     * @param duration Duration for the transition in ms
     */
    public FadeTransitionFactory(long duration) {
        super(duration);
    }

    /**
     * Constructor
     *
     * @param duration     The duration for the transition in ms
     * @param interpolator The kind of interpolator we need
     */
    public FadeTransitionFactory(long duration, Interpolator interpolator) {
        super(duration, interpolator);
    }

    //==============================================================================================
    // INTERFACE IMPLEMENTATION: SlideTransitionFactory
    //==

    @Override
    public ViewPropertyAnimator getInAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        target.setAlpha(0);
        return target.animate().setDuration(getDuration()).setInterpolator(getInterpolator()).alpha(1);
    }

    @Override
    public ViewPropertyAnimator getOutAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        target.setAlpha(1);
        return target.animate().setDuration(getDuration()).setInterpolator(getInterpolator()).alpha(0);
    }
}
