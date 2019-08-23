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

@EqualsAndHashCode(callSuper = true)
@Data
public class TextSlide extends Slide {
    public static final String TYPE_STRING = "text";

    public TextSlide(
            @NonNull final String slideId,
            @NonNull final String slideTitle,
            @NonNull final String slideContent
    ) {
        super(slideId, slideTitle, slideContent);
    }

    @Override
    protected SlideFragment makeFragment() {
        return new TextSlideFragment();
    }

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