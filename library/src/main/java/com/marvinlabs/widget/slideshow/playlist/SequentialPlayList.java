package com.marvinlabs.widget.slideshow.playlist;

import com.marvinlabs.widget.slideshow.PlayList;

/**
 * A list that will play slides in their original order
 * <p/>
 * Created by Vincent Mimoun-Prat @ MarvinLabs on 29/05/2014.
 */
public class SequentialPlayList implements PlayList {

    public static final long DEFAULT_SLIDE_DURATION = 5000;

    // The number of slides in the playlist
    private int slideCount = 0;

    // The slide currently being played
    private int currentSlide = -1;

    // Shall we loop after we have reached the last slide?
    private boolean isLooping = true;

    // Automatically skip to next slide after slideDuration elapsed
    private boolean isAutoAdvanceEnabled = true;

    // Duration in ms for each slide
    private long slideDuration = DEFAULT_SLIDE_DURATION;

    // TODO Allow changing the playlist direction
//    private enum PlayOrder {
//        FORWARD, REVERSE;
//    }
//
//    // The order to play the slides
//    private PlayOrder playOrder;

    //==============================================================================================
    // GENERAL METHODS
    //==

    public SequentialPlayList() {
    }

    //==============================================================================================
    // INTERFACE IMPLEMENTATION: PlayList
    //==

    @Override
    public int getFirstSlide() {
        return slideCount > 0 ? 0 : -1;
    }

    @Override
    public int getCurrentSlide() {
        return currentSlide;
    }

    @Override
    public int getNextSlide() {
        if (currentSlide < slideCount - 1) return currentSlide + 1;
        else if (isLooping) return 0;
        else return -1;
    }

    @Override
    public int getPreviousSlide() {
        if (currentSlide > 0) return currentSlide - 1;
        else if (isLooping) return slideCount - 1;
        else return -1;
    }

    @Override
    public void rewind() {
        currentSlide = -1;
    }

    @Override
    public int next() {
        currentSlide = getNextSlide();
        return currentSlide;
    }

    @Override
    public int previous() {
        currentSlide = getPreviousSlide();
        return currentSlide;
    }

    @Override
    public void onSlideCountChanged(int newSlideCount) {
        this.slideCount = newSlideCount;
        if (currentSlide >= newSlideCount) {
            currentSlide = this.slideCount - 1;
        }
    }

    /**
     * Indicate if the slide show is advancing to the next slide after slideDuration ms are elapsed
     * @return
     */
    public boolean isAutoAdvanceEnabled() {
        return isAutoAdvanceEnabled;
    }

    /**
     * Set if the slide show should advance to the next slide after slideDuration ms are elapsed
     *
     * @param isAutoAdvanceEnabled true to automatically move to next slide after slideDuration ms
     */
    public void setAutoAdvanceEnabled(boolean isAutoAdvanceEnabled) {
        this.isAutoAdvanceEnabled = isAutoAdvanceEnabled;
    }

    //==============================================================================================
    // PLAYLIST CONTROL
    //==

    @Override
    public long getSlideDuration(int position) {
        return slideDuration;
    }

    public void setSlideDuration(long slideDuration) {
        this.slideDuration = slideDuration;
    }

    public boolean isLooping() {
        return isLooping;
    }

    public void setLooping(boolean isLooping) {
        this.isLooping = isLooping;
    }
}
