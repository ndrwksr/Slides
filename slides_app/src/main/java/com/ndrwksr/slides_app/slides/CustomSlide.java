package com.ndrwksr.slides_app.slides;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ndrwksr.slideslib.Slide;
import com.ndrwksr.slideslib.SlideFragment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomSlide extends Slide {
    public static final String TYPE_STRING = "custom_example";

    private final String color;

    public CustomSlide(
            @NonNull final String slideId,
            @NonNull final String slideTitle,
            @NonNull final String slideContent,
            @NonNull final String color
    ) {
        super(slideId, slideTitle, slideContent);
        this.color = color;
    }

    @Override
    public SlideFragment instantiateFragment() {
        return new CustomSlide.CustomSlideFragment();
    }

    @NoArgsConstructor
    public static class CustomSlideFragment extends SlideFragment<CustomSlide> {
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
            final View rootView = inflater.inflate(com.ndrwksr.slideslib.R.layout.slide_text_view, container, false);
            mTextView = rootView.findViewById(com.ndrwksr.slideslib.R.id.slide_text_view);

            mTextView.setText(mSlide.getContent());
            switch (mSlide.getColor()) {
                case "blue":
                    mTextView.setTextColor(Color.BLUE);
                    break;
                case "red":
                    mTextView.setTextColor(Color.RED);
                    break;
            }

            return rootView;
        }
    }
}
