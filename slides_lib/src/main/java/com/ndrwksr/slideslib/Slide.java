package com.ndrwksr.slideslib;

import android.support.annotation.NonNull;

import java.io.Serializable;

import lombok.Data;

@Data
public abstract class Slide implements Serializable {
    public static final String INTENT_NAME = "slide";
    @NonNull
    private final String id;
    @NonNull
    private final String title;
    @NonNull
    private final String content;

    public SlideFragment getFragment() {
        SlideFragment slideFragment = makeFragment();
        slideFragment.mSlide = this;
        return slideFragment;
    }

    protected abstract SlideFragment makeFragment();
}
