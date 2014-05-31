package com.marvinlabs.widget.slideshow;

import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * An SlideTransitionFactory object is responsible for creating the animators to switch from one slide to the next one
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 28/05/2014.
 */
public interface SlideTransitionFactory {

    /**
     * Get the animator that will handle the transition to bring the next slide in
     *
     * @param fromSlide The index of the slide that will disappear
     * @param toSlide   The index of the slide that will appear
     * @return An Animator object to animate the views
     */
    public ViewPropertyAnimator getInAnimator(View target, SlideShowView parent, int fromSlide, int toSlide);

    /**
     * Get the animator that will handle the transition to bring the previous slide out
     *
     * @param fromSlide The index of the slide that will disappear
     * @param toSlide   The index of the slide that will appear
     * @return An Animator object to animate the views
     */
    public ViewPropertyAnimator getOutAnimator(View target, SlideShowView parent, int fromSlide, int toSlide);
}
