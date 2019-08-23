package com.ndrwksr.slideslib.exceptions;

import android.support.annotation.Nullable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BadSlidesException extends Exception {

    public BadSlidesException(@Nullable final String message) {
        super(message);
    }

    public BadSlidesException(@Nullable final String message, @Nullable final Throwable cause) {
        this(message);
        this.initCause(cause);
    }
}
