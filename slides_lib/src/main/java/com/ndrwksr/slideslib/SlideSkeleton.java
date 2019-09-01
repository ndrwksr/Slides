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
import lombok.Data;

/**
 * A data type used to hold the data necessary to parse a Slide. The {@link SlideSkeleton#type}
 * field is used to determine the type of slide this skeleton represents (and must match the key
 * of an entry in {@link SlideLoader#typeTokenMap} to be parsed), and the {@link SlideSkeleton#data}
 * field contains the JSON representation of the slide this skeleton represents.
 */
@AllArgsConstructor
@Data
class SlideSkeleton {
    /**
     * The string type of the {@link Slide} this skeleton represents. Must match the key of an entry
     * in {@link SlideLoader#typeTokenMap} in order to be parsed by that loader.
     */
    private final String type;

    /**
     * The data for the {@link Slide} this skeleton represents. Will be parsed into a slide of the
     * type dictated by {@link SlideSkeleton#type}.
     */
    private final String data;

    /**
     * Constructs a {@link Slide} from this skeleton using the provided map of type strings to type
     * tokens.
     * @param typeTokenMap A map of the type strings and type tokens that this skeleton might
     *                     represent.
     * @return the slide from this skeleton.
     * @throws BadSlideTypeException If the type was null/empty, or no entry with that key was found
     * @throws BadSlideDataException If the data was null/empty or couldn't be parsed.
     */
    @NonNull
    Slide toSlide(
            // TODO: Refactor to only pass the correct type token
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
            throw new BadSlideDataException("Data was empty when converting slide skeleton to slide.", data);
        }

        final Slide slide;
        try {
            slide = (new Gson()).fromJson(data, typeToken.getType());
        } catch (JsonParseException e) {
            throw new BadSlideDataException("Data could not be parsed into a slide.", data, e);
        }

        return slide;
    }
}
