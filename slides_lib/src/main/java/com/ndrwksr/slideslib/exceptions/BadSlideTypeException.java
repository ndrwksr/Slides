package com.ndrwksr.slideslib.exceptions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ndrwksr.slideslib.Slide;
import com.ndrwksr.slideslib.Strings;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Thrown when the type for a {@link Slide} was invalid, either because the type field of a
 * {@link com.ndrwksr.slideslib.SlideSkeleton} was null/empty or because said field wasn't empty but
 * there was no entry in the {@link com.ndrwksr.slideslib.SlideLoader}'s type map with that key.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BadSlideTypeException extends Exception {
    /**
     * The invalid type.
     */
    @Getter
    private final String slideType;

    /**
     * @param message   An explanation for why the exception was thrown.
     * @param slideType The invalid type in the guilty {@link com.ndrwksr.slideslib.SlideSkeleton}.
     */
    public BadSlideTypeException(
            @NonNull final String message,
            @Nullable final String slideType
    ) {
        super(message);
        this.slideType = Strings.clarifyStringIfMissing(slideType);
    }
}
