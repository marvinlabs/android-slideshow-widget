package com.marvinlabs.widget.slideshow.transition;

import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

import com.marvinlabs.widget.slideshow.SlideShowView;

/**
 * A transition maker to zoom the slides in and out
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 28/05/2014.
 */
public class ZoomTransitionFactory extends BaseTransitionFactory {

    public static final float SCALE_FACTOR = 2.5f;

    //==============================================================================================
    // GENERAL METHODS
    //==

    /**
     * Default constuctor
     */
    public ZoomTransitionFactory() {
        super();
    }

    /**
     * Constructor
     *
     * @param duration Duration for the transition in ms
     */
    public ZoomTransitionFactory(long duration) {
        super(duration);
    }

    /**
     * Constructor
     *
     * @param duration     The duration for the transition in ms
     * @param interpolator The kind of interpolator we need
     */
    public ZoomTransitionFactory(long duration, Interpolator interpolator) {
        super(duration, interpolator);
    }

    //==============================================================================================
    // INTERFACE IMPLEMENTATION: SlideTransitionFactory
    //==

    @Override
    public ViewPropertyAnimator getInAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        target.setAlpha(0);
        target.setScaleX(SCALE_FACTOR);
        target.setScaleY(SCALE_FACTOR);
        target.setTranslationX(0);
        target.setTranslationY(0);
        target.setRotationX(0);
        target.setRotationY(0);
        return target.animate().setDuration(getDuration()).setInterpolator(getInterpolator()).alpha(1).scaleX(1).scaleY(1);
    }

    @Override
    public ViewPropertyAnimator getOutAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        return target.animate().setDuration(getDuration()).setInterpolator(getInterpolator()).alpha(0).scaleX(SCALE_FACTOR).scaleY(SCALE_FACTOR);
    }

}
