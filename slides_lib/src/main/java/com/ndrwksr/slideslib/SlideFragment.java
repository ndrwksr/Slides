package com.ndrwksr.slideslib;

import android.support.v4.app.Fragment;

public abstract class SlideFragment<T extends Slide> extends Fragment {
    protected T mSlide;
}
