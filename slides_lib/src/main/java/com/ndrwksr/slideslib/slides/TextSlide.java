package com.ndrwksr.slideslib.slides;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ndrwksr.slideslib.R;
import com.ndrwksr.slideslib.Slide;
import com.ndrwksr.slideslib.SlideFragment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A {@link Slide} which displays the content in a {@link TextView}
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TextSlide extends Slide {
    /**
     * The key for this type of slide in type token maps.
     */
    public static final String TYPE_STRING = "text";

    /**
     * All-args constructor (required for Lombok).
     * @param slideId The ID of the slide.
     * @param slideTitle The title of the slide.
     * @param slideContent The content of the slide.
     */
    public TextSlide(
            @NonNull final String slideId,
            @NonNull final String slideTitle,
            @NonNull final String slideContent
    ) {
        super(slideId, slideTitle, slideContent);
    }

    @Override
    protected SlideFragment instantiateFragment() {
        return new TextSlideFragment();
    }

    /**
     * The {@link android.support.v4.app.Fragment} for {@link TextSlide}.
     */
    @NoArgsConstructor
    public static class TextSlideFragment extends SlideFragment {
        /**
         * The web view for displaying the slide's contents.
         */
        private TextView mTextView;

        @Override
        public View onCreateView(
                @NonNull final LayoutInflater inflater,
                @Nullable final ViewGroup container,
                @Nullable final Bundle savedInstanceState
        ) {
            final View rootView = inflater.inflate(R.layout.slide_text_view, container, false);
            mTextView = rootView.findViewById(R.id.slide_text_view);

            mTextView.setText(mSlide.getContent());

            return rootView;
        }
    }
}