package com.ndrwksr.slideslib;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

/**
 * The abstract superclass for slides. This class stores the ID, title and content of a slide, and
 * defines the interface for getting fragments from slides. Slide is a data type, and should contain
 * no references to or knowledge of the implementation of the respective fragment class.
 * <p>
 * Additional fields can be added to subclasses of Slide, and these fields will be appropriately
 * deserialized from the JSON provided to SlideLoader.
 * <p>
 * Each Slide subclass must provide its own fragment which extends from {@link SlideFragment}, and
 * this fragment class must have its generic type be the slide class to which it belongs.
 * Additionally, each slide should have a public static final String called "TYPE_STRING" which
 * represents the key that slide's type token should be stored under in
 * {@link SlideLoader#typeTokenMap}, and the value of {@link SlideSkeleton#type} for the serialized
 * representation of the slide.
 */
@Data
public abstract class Slide implements Serializable {
    /**
     * The key used to attach a slide to an intent.
     */
    public static final String INTENT_NAME = "slide";

    /**
     * The unique ID of the slide. Can be any valid string, but sequential integers or UUID V6 IDs
     * are recommended.
     */
    @NonNull
    private final String id;

    /**
     * The title of the slide. Will be displayed in the list of slides, and at the top of the slide.
     */
    @NonNull
    private final String title;

    /**
     * The content of the slide. This data will be used differently depending on the type of the
     * slide it belongs to. For example, this field is used to provide the text for a
     * {@link com.ndrwksr.slideslib.slides.TextSlide} and the HTML for an
     * {@link com.ndrwksr.slideslib.slides.HtmlSlide}.
     */
    @NonNull
    private final String content;

    /**
     * @param id      The unique ID of the slide.
     * @param title   The title of the slide.
     * @param content The content of the slide.
     */
    public Slide(
            @NonNull final String id,
            @NonNull final String title,
            @NonNull final String content
    ) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(title);
        Objects.requireNonNull(content);
        this.id = id;
        this.title = title;
        this.content = content;
    }

    /**
     * Used to get the {@link SlideFragment} for this slide. The returned fragment will already have
     * this slide set as the slide for the said fragment.
     *
     * @return The fragment for this slide.
     */
    public SlideFragment makeFragment() {
        SlideFragment slideFragment = instantiateFragment();
        slideFragment.mSlide = this;
        return slideFragment;
    }

    /**
     * Returns a bare instance of the fragment for this slide. Used in {@link Slide#makeFragment()}.
     * @return a bare instance of the fragment for this slide.
     */
    protected abstract SlideFragment instantiateFragment();
}
