package com.marvinlabs.widget.slideshow.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
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
    public Animator getInAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        target.setAlpha(0);
        target.setScaleX(1);
        target.setScaleY(1);
        target.setTranslationX(0);
        target.setTranslationY(0);
        target.setRotationX(0);
        target.setRotationY(0);

        ObjectAnimator animator = ObjectAnimator.ofFloat(target, View.ALPHA, 1);
        animator.setDuration(getDuration());
        animator.setInterpolator(getInterpolator());
        return animator;
    }

    @Override
    public Animator getOutAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, View.ALPHA, 0);
        animator.setDuration(getDuration());
        animator.setInterpolator(getInterpolator());
        return animator;
    }
}
