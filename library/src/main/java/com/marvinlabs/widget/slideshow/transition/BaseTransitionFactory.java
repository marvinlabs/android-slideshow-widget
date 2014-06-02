package com.marvinlabs.widget.slideshow.transition;

import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.marvinlabs.widget.slideshow.TransitionFactory;

/**
 * The base implementation for a transition factory. Provides storage for a duration and an
 * interpolator which should be what is required by most transition factories.
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 02/06/2014.
 */
public abstract class BaseTransitionFactory implements TransitionFactory {

    public static final long DEFAULT_DURATION = 500;
    public static final Interpolator DEFAULT_INTERPOLATOR = new DecelerateInterpolator();

    // Duration for the transition in ms
    private long duration = DEFAULT_DURATION;

    // Interpolator to use for the transition
    private Interpolator interpolator;

    //==============================================================================================
    // GENERAL METHODS
    //==

    /**
     * Default constructor
     */
    public BaseTransitionFactory() {
        this(DEFAULT_DURATION, DEFAULT_INTERPOLATOR);
    }

    /**
     * Constructor
     *
     * @param duration The duration for the transition in ms
     */
    public BaseTransitionFactory(long duration) {
        this(duration, DEFAULT_INTERPOLATOR);
    }

    /**
     * Constructor
     *
     * @param duration     The duration for the transition in ms
     * @param interpolator The kind of interpolator we need
     */
    public BaseTransitionFactory(long duration, Interpolator interpolator) {
        this.interpolator = interpolator;
        this.duration = duration;
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
}
