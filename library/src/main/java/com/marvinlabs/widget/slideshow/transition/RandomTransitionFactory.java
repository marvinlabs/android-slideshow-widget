package com.marvinlabs.widget.slideshow.transition;

import android.animation.Animator;
import android.view.View;
import android.view.animation.Interpolator;

import com.marvinlabs.widget.slideshow.SlideShowView;
import com.marvinlabs.widget.slideshow.TransitionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A factory that will pick a random transition for the slides. This assumes that the IN animator is
 * always requested before the OUT animator.
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 02/06/2014.
 */
public class RandomTransitionFactory extends BaseTransitionFactory {

    private static final Random RAND = new Random();

    private final List<TransitionFactory> factories;

    private TransitionFactory currentFactory;

    static {
        RAND.setSeed(System.currentTimeMillis());
    }

    //==============================================================================================
    // GENERAL METHODS
    //==

    /**
     * Default constuctor
     */
    public RandomTransitionFactory() {
        this(DEFAULT_DURATION, DEFAULT_INTERPOLATOR);
    }

    /**
     * Constructor
     *
     * @param duration Duration for the transition in ms
     */
    public RandomTransitionFactory(long duration) {
        this(duration, DEFAULT_INTERPOLATOR);
    }

    /**
     * Constructor
     *
     * @param duration     The duration for the transition in ms
     * @param interpolator The kind of interpolator we need
     */
    public RandomTransitionFactory(long duration, Interpolator interpolator) {
        super(duration, interpolator);

        factories = new ArrayList<TransitionFactory>();
        factories.add(new FadeTransitionFactory(getDuration(), getInterpolator()));
        factories.add(new ZoomTransitionFactory(getDuration(), getInterpolator()));
        factories.add(new SlideAndZoomTransitionFactory(getDuration(), getInterpolator()));
        factories.add(new FlipTransitionFactory(getDuration(), getInterpolator()));
    }

    //==============================================================================================
    // INTERFACE IMPLEMENTATION: SlideTransitionFactory
    //==

    @Override
    public Animator getInAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        // Pick a factory for this round of transition
        int r = RAND.nextInt(factories.size());
        currentFactory = factories.get(r);

        return currentFactory.getInAnimator(target, parent, fromSlide, toSlide);
    }

    @Override
    public Animator getOutAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        // Factory has been picked when calling getInAnimator
        return currentFactory.getOutAnimator(target, parent, fromSlide, toSlide);
    }
}
