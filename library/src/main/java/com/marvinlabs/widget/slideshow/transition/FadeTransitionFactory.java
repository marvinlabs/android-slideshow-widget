package com.marvinlabs.widget.slideshow.transition;

import android.view.View;
import android.view.ViewPropertyAnimator;

import com.marvinlabs.widget.slideshow.SlideShowView;
import com.marvinlabs.widget.slideshow.SlideTransitionFactory;

/**
 * A transition maker to fade the slides in and out
 *
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 28/05/2014.
 */
public class FadeTransitionFactory implements SlideTransitionFactory {

    private static final long DEFAULT_FADE_DURATION = 1000;

    private long duration = DEFAULT_FADE_DURATION;

    @Override
    public ViewPropertyAnimator getInAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        target.setAlpha(0);
        return target.animate().setDuration(getDuration()).alpha(1);
    }

    @Override
    public ViewPropertyAnimator getOutAnimator(View target, SlideShowView parent, int fromSlide, int toSlide) {
        target.setAlpha(1);
        return target.animate().setDuration(getDuration()).alpha(0);
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
