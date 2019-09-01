package com.ndrwksr.slideslib;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.ndrwksr.slideslib.exceptions.BadSlideDataException;
import com.ndrwksr.slideslib.exceptions.BadSlideTypeException;
import com.ndrwksr.slideslib.exceptions.BadSlidesException;
import com.ndrwksr.slideslib.slides.DocSlide;
import com.ndrwksr.slideslib.slides.HtmlFileSlide;
import com.ndrwksr.slideslib.slides.HtmlSlide;
import com.ndrwksr.slideslib.slides.TextSlide;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.Getter;

/**
 * A utility class used to deserialize slides. Slides are provided in JSON format, and the loader
 * will deserialize said JSON into {@link SlideSkeleton}s. The skeletons are used to specify
 * the type of each slide, and the data for said slide. The {@link SlideSkeleton#type} field must be
 * the key for an entry in {@link SlideLoader#typeTokenMap}, and the {@link SlideSkeleton#data}
 * field must be a valid JSON representation of the slide specified by the type.
 * <p>
 * The {@link Slide} subclasses provided by this library are included in
 * {@link SlideLoader#typeTokenMap} by default, and new slide types can be added to this list by
 * invoking {@link SlideLoader#registerSlideTypes(Map)} with a map containing the type strings as
 * keys and the respective type tokes as values. See the slides_app module for an example of adding
 * a custom type of slide (in both the "slides" package and the "SlideListActivity" class).
 * <p>
 * The {@link SlideLoader#parseSlides(String)} method does not throw any exceptions it encounters
 * while loading individual slides, but will instead append them to
 * {@link SlideLoader#parseExceptions}.
 */
public class SlideLoader {
    /**
     * The log tag for errors when loading the slides from the provided JSON.
     */
    public static final String PARSE_EXCEPTION_TAG = "PARSE";

    /**
     * The exceptions encountered last time {@link SlideLoader#parseSlides(String)} was invoked.
     */
    @Getter
    private final List<Exception> parseExceptions = new ArrayList<>();

    /**
     * The last slides which were loaded, used to cache the result of parsing slides.
     */
    private List<Slide> slideList = new ArrayList<>();

    /**
     * The hash of the last JSON to be parsed, used to cache the result of parsing slides.
     * A value of 0 indicates no slides have been parsed.
     */
    private int lastSlideListHash = 0;

    /**
     * A map of the slide types where the key is the string for that slide type (ex. "text" for
     * TextSlide) and the value is the {@link TypeToken} for the slide type
     * (ex. TypeToken&lt;TextSlide&gt; for TextSlide).
     */
    private final Map<String, TypeToken<? extends Slide>> typeTokenMap = makeDefaultTokenMap();

    /**
     * Returns a map of the built-in slides types, where the key is the string type and the value
     * is the {@link TypeToken} of the {@link Slide} subclass.
     *
     * @return a map of the built-in slides types.
     */
    public static Map<String, TypeToken<? extends Slide>> makeDefaultTokenMap() {
        final Map<String, TypeToken<? extends Slide>> typeTokenMap = new HashMap<>();
        typeTokenMap.put(DocSlide.TYPE_STRING, new TypeToken<DocSlide>() {
        });
        typeTokenMap.put(HtmlFileSlide.TYPE_STRING, new TypeToken<HtmlFileSlide>() {
        });
        typeTokenMap.put(HtmlSlide.TYPE_STRING, new TypeToken<HtmlSlide>() {
        });
        typeTokenMap.put(TextSlide.TYPE_STRING, new TypeToken<TextSlide>() {
        });
        return typeTokenMap;
    }

    /**
     * Returns an unmodifiable copy of {@link SlideLoader#typeTokenMap}. This method should
     * <b>NOT</b> be used for adding new slide types, that can only be accomplished with
     * {@link SlideLoader#registerSlideTypes(Map)}.
     *
     * @return an unmodifiable copy of {@link SlideLoader#typeTokenMap}.
     */
    @NonNull
    public Map<String, TypeToken<? extends Slide>> getTypeTokenMap() {
        return Collections.unmodifiableMap(typeTokenMap);
    }

    /**
     * Load slides from provided {@link InputStream} if the JSON provided doesn't hash to the same
     * thing as the last JSON provided. This method is a wrapper around
     * {@link SlideLoader#parseSlides(String)} which caches the results of that more expensive
     * method.
     *
     * @param inputStream A stream containing the input JSON.
     * @param forceReload True if the cached results should not be used, false otherwise.
     * @return The parsed (or cached) slides from the provided JSON.
     * @throws BadSlidesException If the provided JSON couldn't be read or was invalid.
     */
    @NonNull
    private List<Slide> loadSlides(
            @NonNull final InputStream inputStream,
            final boolean forceReload
    ) throws BadSlidesException {
        final String streamContent;
        try {
            streamContent = Strings.getStringFromStream(inputStream);
        } catch (IOException e) {
            throw new BadSlidesException("Couldn't get JSON for slides from stream", null, e);
        }

        final List<Slide> loadedSlides;
        if (!forceReload &&
                lastSlideListHash != 0 &&
                streamContent.hashCode() != lastSlideListHash) {
            loadedSlides = parseSlides(streamContent);
        } else {
            loadedSlides = parseSlides(streamContent);
        }

        return loadedSlides;
    }

    /**
     * Parses the provided JSON into slides. The JSON is first parsed into a list of
     * {@link SlideSkeleton}s, and then the {@link SlideSkeleton#type} field is used to get the
     * {@link TypeToken} for that slide type from {@link SlideLoader#typeTokenMap}. Then, the data
     * from {@link SlideSkeleton#data} is parsed into a {@link Slide} subclass dictated by the
     * type token. This allows for new fields to be added to slide subclasses.
     *
     * @param slidesJson The JSON for the slides.
     * @return the slides from the provided JSON.
     * @throws BadSlidesException If the JSON couldn't be parsed into a list of
     *                            {@link SlideSkeleton}
     */
    private List<Slide> parseSlides(
            @NonNull final String slidesJson
    ) throws BadSlidesException {
        // Clear exceptions from last parse operation
        parseExceptions.clear();

        // Make list to hold slides as we deserialize them
        final List<Slide> loadedSlides = new ArrayList<>();

        // Get slide skeletons from file
        final Gson gson = new Gson();
        final SlideSkeleton[] slideSkeletons;
        try {
            slideSkeletons = gson.fromJson(slidesJson, SlideSkeleton[].class);
        } catch (JsonParseException e) {
            throw new BadSlidesException("Couldn't parse slide skeletons", slidesJson, e);
        }

        for (@Nullable final SlideSkeleton slideSkeleton : slideSkeletons) {
            if (slideSkeleton == null) {
                BadSlideDataException e = new BadSlideDataException("Got null slide skeleton.", null);
                Log.e(PARSE_EXCEPTION_TAG, "Got null slide skeleton.", e);
                parseExceptions.add(e);
            } else {
                try {
                    // If skeleton was successfully deserialized, add to loadedSlides
                    // TODO: Refactor to only pass the correct type token
                    final Slide slide = slideSkeleton.toSlide(typeTokenMap);
                    loadedSlides.add(slide);
                } catch (BadSlideTypeException e) { // Else log failure and skip this skeleton
                    Log.e(PARSE_EXCEPTION_TAG, "Got bad slide type: " + e.getSlideType(), e);
                    parseExceptions.add(e);
                } catch (BadSlideDataException e) {
                    Log.e(PARSE_EXCEPTION_TAG, "Got bad slide data: " + e.getSlideData(), e);
                    parseExceptions.add(e);
                }
            }
        }

        lastSlideListHash = slidesJson.hashCode();
        return loadedSlides;
    }

    /**
     * Adds all entries in the provided map to {@link SlideLoader#typeTokenMap}. Used to register
     * custom slide types.
     *
     * @param newTypeTokenMapEntries The new slide types to register, where the key is the string
     *                               type and the value is the {@link TypeToken}
     * @throws BadSlideTypeException If the type string was null/empty, or the type token was null
     *                               for any entries in the provided list.
     */
    public void registerSlideTypes(
            @NonNull final Map<String, TypeToken<? extends Slide>> newTypeTokenMapEntries
    ) throws BadSlideTypeException {
        Objects.requireNonNull(newTypeTokenMapEntries, "newTypeTokenMapEntries is required.");
        for (final Map.Entry<String, TypeToken<? extends Slide>> entry : newTypeTokenMapEntries.entrySet()) {
            if (Strings.isEmpty(entry.getKey())) {
                throw new BadSlideTypeException("No key in type map entry", entry.getKey());
            }
            if (entry.getValue() == null) {
                throw new BadSlideTypeException("No value in type map entry", entry.getKey());
            }
            this.typeTokenMap.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Force loads the slides from the provided JSON. The result of this load will be cached, but
     * the cached slides from the previous load will be discarded.
     *
     * @param inputStream The stream containing the JSON to load the slides from.
     * @return The slides contained in the provided JSON.
     * @throws BadSlidesException If the provided JSON couldn't be read or was invalid.
     */
    @NonNull
    public List<Slide> reloadSlides(
            @NonNull final InputStream inputStream
    ) throws BadSlidesException {
        Objects.requireNonNull(inputStream, "inputStream is required.");
        slideList = loadSlides(inputStream, true);
        return slideList;
    }

    /**
     * Loads the slides from the provided JSON. If the hash of the JSON provided matches the hash of
     * the JSON provided in the last successful load operation, the cached slides from the previous
     * load will be returned without re-parsing the provided JSON. If the hash from the last load is
     * 0 or doesn't match the hash of the provided JSON, then the JSON will be parsed into a list of
     * slides, and said list will be returned. The result of this operation will be cached.
     *
     * @param inputStream The stream containing the JSON to load the slides from.
     * @return The slides contained in the provided JSON.
     * @throws BadSlidesException If the provided JSON couldn't be read or was invalid.
     */
    @NonNull
    public List<Slide> getSlides(
            @NonNull final InputStream inputStream
    ) throws BadSlidesException {
        Objects.requireNonNull(inputStream, "inputStream is required.");
        slideList = loadSlides(inputStream, false);
        return slideList;
    }
}
