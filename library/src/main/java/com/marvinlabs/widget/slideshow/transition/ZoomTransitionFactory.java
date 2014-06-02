package com.marvinlabs.widget.slideshow.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
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
    public Animator getInAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        target.setAlpha(0);
        target.setScaleX(SCALE_FACTOR);
        target.setScaleY(SCALE_FACTOR);
        target.setTranslationX(0);
        target.setTranslationY(0);
        target.setRotationX(0);
        target.setRotationY(0);

        final PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1);
        final PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1);
        final PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target, scaleX, scaleY, alpha);
        animator.setDuration(getDuration());
        animator.setInterpolator(getInterpolator());

        return animator;
    }

    @Override
    public Animator getOutAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, SCALE_FACTOR);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, SCALE_FACTOR);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target, scaleX, scaleY, alpha);
        animator.setDuration(getDuration());
        animator.setInterpolator(getInterpolator());

        return animator;
    }

}
