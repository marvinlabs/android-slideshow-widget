package com.marvinlabs.widget.slideshow.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.marvinlabs.widget.slideshow.SlideShowView;

/**
 * A transition maker to flip the slides like cards.
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 02/06/2014.
 */
public class FlipTransitionFactory extends BaseTransitionFactory {

    public enum FlipAxis {
        HORIZONTAL, VERTICAL;
    }

    private FlipAxis axis;

    //==============================================================================================
    // GENERAL METHODS
    //==

    /**
     * Default constuctor
     */
    public FlipTransitionFactory() {
        this(DEFAULT_DURATION, new LinearInterpolator(), FlipAxis.VERTICAL);
    }

    /**
     * Constructor
     *
     * @param duration Duration for the transition in ms
     */
    public FlipTransitionFactory(long duration) {
        this(duration, new LinearInterpolator(), FlipAxis.VERTICAL);
    }

    /**
     * Constructor
     *
     * @param duration     The duration for the transition in ms
     * @param interpolator The kind of interpolator we need
     */
    public FlipTransitionFactory(long duration, Interpolator interpolator) {
        this(duration, interpolator, FlipAxis.VERTICAL);
    }

    /**
     * Constructor
     *
     * @param duration     The duration for the transition in ms
     * @param axis The axis on which we want to flip
     */
    public FlipTransitionFactory(long duration, FlipAxis axis) {
        this(duration, new LinearInterpolator(), axis);
    }

    /**
     * Constructor
     *
     * @param duration     The duration for the transition in ms
     * @param interpolator The kind of interpolator we need
     */
    public FlipTransitionFactory(long duration, Interpolator interpolator, FlipAxis axis) {
        super(duration, interpolator);
        this.axis = axis;
    }

    /**
     * Get the flip axis
     *
     * @return The flip axis
     */
    public FlipAxis getAxis() {
        return axis;
    }

    /**
     * Set the flip axis
     *
     * @param axis the flip axis
     */
    public void setDirection(FlipAxis axis) {
        this.axis = axis;
    }

    //==============================================================================================
    // INTERFACE IMPLEMENTATION: SlideTransitionFactory
    //==

    @Override
    public Animator getInAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        target.setAlpha(1);
        target.setScaleX(1);
        target.setScaleY(1);
        target.setTranslationX(0);
        target.setTranslationY(0);

        final PropertyValuesHolder rotation;
        final PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0, 1);

        switch (axis) {
            case VERTICAL:
                target.setRotationX(-90);
                target.setRotationY(0);
                rotation = PropertyValuesHolder.ofFloat(View.ROTATION_X, 0);
                break;

            case HORIZONTAL:
            default:
                target.setRotationX(0);
                target.setRotationY(-90);
                rotation = PropertyValuesHolder.ofFloat(View.ROTATION_Y, 0);
                break;
        }

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target, rotation/*, alpha*/);
        animator.setDuration(getDuration()/2);
        animator.setStartDelay(getDuration()/2);
        animator.setInterpolator(getInterpolator());

        return animator;
    }

    @Override
    public Animator getOutAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        final PropertyValuesHolder rotation;
        final PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1, 0);

        switch (axis) {
            case VERTICAL:
                target.setRotationX(0);
                target.setRotationY(0);
                rotation = PropertyValuesHolder.ofFloat(View.ROTATION_X, 90);
                break;

            case HORIZONTAL:
            default:
                target.setRotationX(0);
                target.setRotationY(0);
                rotation = PropertyValuesHolder.ofFloat(View.ROTATION_Y, 90);
                break;
        }

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target, rotation/*, alpha*/);
        animator.setDuration(getDuration()/2);
        animator.setInterpolator(getInterpolator());

        return animator;
    }

}
