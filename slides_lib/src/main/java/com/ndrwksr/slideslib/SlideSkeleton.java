package com.ndrwksr.slideslib;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.ndrwksr.slideslib.exceptions.BadSlideDataException;
import com.ndrwksr.slideslib.exceptions.BadSlideTypeException;

import java.util.Map;
import java.util.Objects;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class SlideSkeleton {
    private final String type;
    private final String data;

    @NonNull
    Slide toSlide(
            @NonNull final Map<String, TypeToken<? extends Slide>> typeTokenMap
    ) throws BadSlideTypeException, BadSlideDataException {
        Objects.requireNonNull(typeTokenMap, "typeTokenMap is required.");

        if (Strings.isEmpty(type)) {
            throw new BadSlideTypeException(type, null);
        }

        final TypeToken<? extends Slide> typeToken = typeTokenMap.get(type);
        if (typeToken == null) {
            throw new BadSlideTypeException(type, null);
        }

        if (Strings.isEmpty(data)) {
            throw new BadSlideDataException(data);
        }

        final Slide slide;
        try {
            slide = (new Gson()).fromJson(data, typeToken.getType());
        } catch (JsonParseException e) {
            throw new BadSlideDataException(data);
        }

        return slide;
    }
}
