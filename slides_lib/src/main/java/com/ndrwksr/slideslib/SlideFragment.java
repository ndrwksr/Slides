package com.ndrwksr.slideslib;

import android.support.v4.app.Fragment;

/**
 * The fragment for a {@link Slide}. Each slide must subclass SlideFragment (with the generic type
 * of the fragment set to the type of the slide subclass).
 *
 * @param <T> The type of the slide this fragment belongs to.
 */
public abstract class SlideFragment<T extends Slide> extends Fragment {
    /**
     * The slide data for this fragment.
     */
    protected T mSlide;
}
