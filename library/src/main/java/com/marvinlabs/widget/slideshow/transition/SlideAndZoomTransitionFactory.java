package com.marvinlabs.widget.slideshow.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.Interpolator;

import com.marvinlabs.widget.slideshow.SlideShowView;

/**
 * A transition maker to slide out left and zoom in
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 28/05/2014.
 */
public class SlideAndZoomTransitionFactory extends BaseTransitionFactory {

    //==============================================================================================
    // GENERAL METHODS
    //==

    /**
     * Default constuctor
     */
    public SlideAndZoomTransitionFactory() {
        super();
    }

    /**
     * Constructor
     *
     * @param duration Duration for the transition in ms
     */
    public SlideAndZoomTransitionFactory(long duration) {
        super(duration);
    }

    /**
     * Constructor
     *
     * @param duration     The duration for the transition in ms
     * @param interpolator The kind of interpolator we need
     */
    public SlideAndZoomTransitionFactory(long duration, Interpolator interpolator) {
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

        final PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, parent.getWidth(), parent.getWidth()/3, 0);
        final PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.5f, 0.6f, 1.0f);
        final PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 0.6f, 1.0f);
        final PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0, 1, 1);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target, translationX, scaleX, scaleY, alpha);
        animator.setDuration(getDuration());
        animator.setInterpolator(getInterpolator());

        return animator;
    }

    @Override
    public Animator getOutAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        final PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0, -parent.getWidth()/3, -parent.getWidth());
        final PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f, 0.6f, 0.5f);
        final PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f, 0.6f, 0.5f);
        final PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1, 1, 0);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target, translationX, scaleX, scaleY, alpha);
        animator.setDuration(getDuration());
        animator.setInterpolator(getInterpolator());

        return animator;
    }

}
