package com.ndrwksr.slideslib.exceptions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ndrwksr.slideslib.Strings;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Thrown when the data in a {@link com.ndrwksr.slideslib.SlideSkeleton} is invalid (meaning it was
 * null or couldn't be parsed into a {@link com.ndrwksr.slideslib.Slide}).
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BadSlideDataException extends Exception {
    /**
     * The invalid data.
     */
    private final String slideData;

    /**
     * @param message   An explanation for why the exception was thrown.
     * @param slideData The invalid slide data.
     */
    public BadSlideDataException(
            @NonNull final String message,
            @Nullable final String slideData
    ) {
        super(message);
        this.slideData = Strings.clarifyStringIfMissing(slideData);
    }


    /**
     * @param message   An explanation for why the exception was thrown.
     * @param slideData The invalid slide data.
     * @param cause     The cause of the exception (usually related to parsing the data).
     */
    public BadSlideDataException(
            @NonNull final String message,
            @Nullable final String slideData,
            @Nullable final Throwable cause
    ) {
        this(message, slideData);
        this.initCause(cause);
    }
}
