package com.ndrwksr.slideslib.exceptions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Thrown when the JSON provided to a {@link com.ndrwksr.slideslib.SlideLoader} can't be parsed into
 * a list of {@link com.ndrwksr.slideslib.SlideSkeleton}s.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BadSlidesException extends Exception {
    /**
     * The JSON containing slides that was being parsed when the exception was thrown. If this is
     * null, then the {@link com.ndrwksr.slideslib.SlideLoader} never got the JSON it was supposed
     * to parse.
     */
    final String slidesJson;

    /**
     * @param message    An explanation for why the exception was thrown.
     * @param slidesJson The JSON containing the slides.
     * @param cause      The cause of the exception (usually related to parsing the data).
     */
    public BadSlidesException(
            @NonNull final String message,
            @Nullable final String slidesJson,
            @Nullable final Throwable cause
    ) {
        super(message);
        this.slidesJson = slidesJson;
        this.initCause(cause);
    }
}
