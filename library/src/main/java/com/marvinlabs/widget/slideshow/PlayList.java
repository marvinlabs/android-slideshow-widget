package com.marvinlabs.widget.slideshow;

/**
 * Created by vprat on 29/05/2014.
 */
public interface PlayList {

    /**
     * Get the index of the slide currently being played
     *
     * @return -1 if the current slide is not available. A positive integer otherwise.
     */
    public int getCurrentSlide();

    /**
     * Get the index of the first slide to play
     *
     * @return -1 if there will be nothing to play. A positive integer otherwise.
     */
    public int getFirstSlide();

    /**
     * Get the index of the next slide in the play list
     *
     * @return -1 if the next slide is not available. A positive integer otherwise.
     */
    public int getNextSlide();

    /**
     * Get the index of the previous slide in the play list
     *
     * @return -1 if the previous slide is not available. A positive integer otherwise.
     */
    public int getPreviousSlide();

    /**
     * Rewind the play list to the first slide
     */
    public void rewind();

    /**
     * Moves the playlist to the next slide
     *
     * @return The new current slide index
     */
    public int next();

    /**
     * Moves the playlist to the previous slide
     *
     * @return The new current slide index
     */
    public int previous();

    /**
     * This function is called when the number of slides has changed
     */
    public void onSlideCountChanged(int newSlideCount);

    /**
     * Get the duration for the given slide
     *
     * @return The duration in ms
     */
    public long getSlideDuration(int position);

    /**
     * Indicate if the slide show is advancing to the next slide after slideDuration ms are elapsed
     * @return
     */
    public boolean isAutoAdvanceEnabled();
}
