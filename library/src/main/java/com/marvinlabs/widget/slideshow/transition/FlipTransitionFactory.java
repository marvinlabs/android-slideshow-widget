package com.marvinlabs.widget.slideshow.transition;

import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

import com.marvinlabs.widget.slideshow.SlideShowView;

/**
 * A transition maker to flip the slides like cards.
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 02/06/2014.
 */
public class FlipTransitionFactory extends BaseTransitionFactory {

    public enum FlipDirection {
        HORIZONTAL, VERTICAL;
    }

    private FlipDirection direction;

    //==============================================================================================
    // GENERAL METHODS
    //==

    /**
     * Default constuctor
     */
    public FlipTransitionFactory() {
        this(DEFAULT_DURATION, DEFAULT_INTERPOLATOR, FlipDirection.VERTICAL);
    }

    /**
     * Constructor
     *
     * @param duration Duration for the transition in ms
     */
    public FlipTransitionFactory(long duration) {
        this(duration, DEFAULT_INTERPOLATOR, FlipDirection.VERTICAL);
    }

    /**
     * Constructor
     *
     * @param duration     The duration for the transition in ms
     * @param interpolator The kind of interpolator we need
     */
    public FlipTransitionFactory(long duration, Interpolator interpolator) {
        this(duration, interpolator, FlipDirection.VERTICAL);
    }

    /**
     * Constructor
     *
     * @param duration     The duration for the transition in ms
     * @param interpolator The kind of interpolator we need
     */
    public FlipTransitionFactory(long duration, Interpolator interpolator, FlipDirection direction) {
        super(duration, interpolator);
        this.direction = direction;
    }

    /**
     * Get the flip direction
     *
     * @return The flip direction
     */
    public FlipDirection getDirection() {
        return direction;
    }

    /**
     * Set the flip direction
     *
     * @param direction the flip direction
     */
    public void setDirection(FlipDirection direction) {
        this.direction = direction;
    }

    //==============================================================================================
    // INTERFACE IMPLEMENTATION: SlideTransitionFactory
    //==

    @Override
    public ViewPropertyAnimator getInAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        target.setAlpha(1);
        target.setScaleX(1);
        target.setScaleY(1);
        target.setTranslationX(0);
        target.setTranslationY(0);
        target.setRotationX(0);

        final ViewPropertyAnimator baseAnimator = target.animate().setDuration(getDuration() / 2).setStartDelay(getDuration() / 2).setInterpolator(getInterpolator());
        switch (direction) {
            case HORIZONTAL:
                target.setRotationX(-90);
                baseAnimator.rotationX(0);
                break;

            case VERTICAL:
                target.setRotationY(-90);
                baseAnimator.rotationY(0);
                break;
        }
        return baseAnimator;
    }

    @Override
    public ViewPropertyAnimator getOutAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        final ViewPropertyAnimator baseAnimator = target.animate().setDuration(getDuration() / 2).setStartDelay(getDuration() / 2).setInterpolator(getInterpolator());
        switch (direction) {
            case HORIZONTAL:
                baseAnimator.rotationX(90);
                break;

            case VERTICAL:
                baseAnimator.rotationY(90);
                break;
        }
        return baseAnimator;
    }

}
