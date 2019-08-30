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

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.Getter;

public class SlideLoader {
    public static final String PARSE_EXCEPTION_TAG = "PARSE";

    @Getter
    private final List<Exception> loadExceptions = new ArrayList<>();

    private List<Slide> slideList = null;

    private final Map<String, TypeToken<? extends Slide>> typeTokenMap = makeDefaultTokenMap();

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

    @NonNull
    public Map<String, TypeToken<? extends Slide>> getTypeTokenMap() {
        return Collections.unmodifiableMap(typeTokenMap);
    }

    // Load slides from provided reader
    // Stores per-slide exceptions in loadExceptions so that bad slides can be ignored
    // BadSlidesException will be thrown if slide skeletons can't be read
    @NonNull
    private List<Slide> loadSlides(@NonNull final InputStreamReader inputStreamReader) throws BadSlidesException {
        // Clear exceptions from last load operation
        loadExceptions.clear();

        // Get slide skeletons from file
        final Gson gson = new Gson();
        final SlideSkeleton[] slideSkeletons;
        try {
            slideSkeletons = gson.fromJson(inputStreamReader, SlideSkeleton[].class);
        } catch (JsonParseException e) {
            throw new BadSlidesException("Couldn't read slide skeletons", e);
        }

        // Make list to hold slides as we deserialize them
        final List<Slide> loadedSlides = new ArrayList<>();

        for (@Nullable final SlideSkeleton slideSkeleton : slideSkeletons) {
            if (slideSkeleton == null) {
                BadSlideDataException e = new BadSlideDataException("Got null slide skeleton.");
                Log.e(PARSE_EXCEPTION_TAG, "Got null slide skeleton.", e);
                loadExceptions.add(e);
            } else {
                try {
                    // If skeleton was successfully deserialized, add to loadedSlides
                    final Slide slide = slideSkeleton.toSlide(typeTokenMap);
                    loadedSlides.add(slide);
                } catch (BadSlideTypeException e) { // Else log failure and skip this skeleton
                    Log.e(PARSE_EXCEPTION_TAG, "Got bad slide type: " + e.getSlideType(), e);
                    loadExceptions.add(e);
                } catch (BadSlideDataException e) {
                    Log.e(PARSE_EXCEPTION_TAG, "Got bad slide data: " + e.getSlideData(), e);
                    loadExceptions.add(e);
                }
            }
        }

        return loadedSlides;
    }

    public void registerSlideTypes(
            @NonNull final Map<String, TypeToken<? extends Slide>> newTypeTokenMapEntries
    ) throws BadSlideTypeException {
        Objects.requireNonNull(newTypeTokenMapEntries, "newTypeTokenMapEntries is required.");
        for (final Map.Entry<String, TypeToken<? extends Slide>> entry : newTypeTokenMapEntries.entrySet()) {
            if (Strings.isEmpty(entry.getKey())) {
                throw new BadSlideTypeException(entry.getKey(), entry.getValue());
            }
            if (entry.getValue() == null) {
                throw new BadSlideTypeException(entry.getKey(), null);
            }
            this.typeTokenMap.put(entry.getKey(), entry.getValue());
        }
    }

    // Force loading slides and return them
    @NonNull
    public List<Slide> reloadSlides(@NonNull final InputStreamReader inputStreamReader) throws BadSlidesException {
        Objects.requireNonNull(inputStreamReader, "inputStreamReader is required.");
        slideList = loadSlides(inputStreamReader);
        return slideList;
    }

    // Load slides if not already loaded, otherwise return them
    @NonNull
    public List<Slide> getSlides(@NonNull final InputStreamReader inputStreamReader) throws BadSlidesException {
        Objects.requireNonNull(inputStreamReader, "inputStreamReader is required.");
        slideList = slideList == null ? loadSlides(inputStreamReader) : slideList;
        return slideList;
    }
}
