package com.example.slides.slideFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.slides.R;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TextSlideFragment extends SlideFragment {
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

        mTextView.setText(mSlideListItem.getSlideContent());

        return rootView;
    }
}