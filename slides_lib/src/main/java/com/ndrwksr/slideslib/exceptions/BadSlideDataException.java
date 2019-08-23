package com.ndrwksr.slideslib.exceptions;

import android.support.annotation.Nullable;

import com.ndrwksr.slideslib.Strings;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BadSlideDataException extends Exception {
    private final String slideData;

    public BadSlideDataException(@Nullable final String slideData) {
        super("Bad slide data: " + Strings.clarifyStringIfMissing(slideData));
        this.slideData = slideData;
    }

    public BadSlideDataException(@Nullable final String slideData, @Nullable final Throwable cause) {
        this(slideData);
        this.initCause(cause);
    }
}
