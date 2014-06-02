package com.marvinlabs.widget.slideshow.transition;

import android.animation.Animator;
import android.view.View;

import com.marvinlabs.widget.slideshow.SlideShowView;
import com.marvinlabs.widget.slideshow.TransitionFactory;

/**
 * A transition maker to avoid any transition
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 28/05/2014.
 */
public class NoTransitionFactory implements TransitionFactory {

    //==============================================================================================
    // INTERFACE IMPLEMENTATION: SlideTransitionFactory
    //==

    @Override
    public Animator getInAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        return null;
    }

    @Override
    public Animator getOutAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        return null;
    }
}
