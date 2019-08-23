package com.ndrwksr.slideslib.exceptions;

import android.support.annotation.Nullable;

import com.google.gson.reflect.TypeToken;
import com.ndrwksr.slideslib.Strings;
import com.ndrwksr.slideslib.Slide;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Data
public class BadSlideTypeException extends Exception {
    @Getter
    private final String slideType;
    @Getter
    private final TypeToken<? extends Slide> typeToken;

    public BadSlideTypeException(
            @Nullable final String slideType,
            @Nullable final TypeToken<? extends Slide> typeToken
    ) {
        super("Bad slide type: " + Strings.clarifyStringIfMissing(slideType) + "; " +
                "type token: " + Strings.clarifyStringIfMissing(
                typeToken == null ? null : typeToken.toString())
        );
        this.slideType = slideType;
        this.typeToken = typeToken;
    }

    public BadSlideTypeException(
            @Nullable final String slideType,
            @Nullable final TypeToken<? extends Slide> typeToken,
            @Nullable final Throwable cause
    ) {
        this(slideType, typeToken);
        this.initCause(cause);
    }
}
