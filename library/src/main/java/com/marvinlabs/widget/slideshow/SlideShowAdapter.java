package com.marvinlabs.widget.slideshow;

import android.widget.Adapter;

/**
 * A SlideAdapter object acts as a bridge between a SlideShowView and the underlying SlideShow.
 * The SlideAdapter is responsible for making a View for each slide in the slideshow.
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 28/05/2014.
 */
public interface SlideShowAdapter extends Adapter {

    public enum SlideStatus {
        READY, LOADING, NOT_AVAILABLE;
    }

    /**
     * This function is called when a slide is scheduled to be next to play. You can take this opportunity to do some work to get
     * that slide ready to play (e.g. download the corresponding image)
     *
     * @param position The next slide index
     */
    public void prepareSlide(int position);

    /**
     * This function is called when a slide won't be needed anymore (until it gets prepared again). This is an opportunity to release
     * memory corresponding to the slide that got prepared previously.
     *
     * @param position The slide index
     */
    public void discardSlide(int position);

    /**
     * Is the slide at that position ready to be played? The slide show will wait until that slide is ready to be played or until the adapter
     * says the slide will never be ready and thus should skip it.
     *
     * @param position The slide index
     * @return The status of the given slide
     */
    public SlideStatus getSlideStatus(int position);

}
