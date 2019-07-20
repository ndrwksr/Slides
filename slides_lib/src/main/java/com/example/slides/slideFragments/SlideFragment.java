package com.example.slides.slideFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.slides.SlideListItem;

public abstract class SlideFragment extends Fragment {
    protected SlideListItem mSlideListItem;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        assert arguments != null;
        // Load the dummy content specified by the fragment
        // arguments. In a real-world scenario, use a Loader
        // to load content from a content provider.
        mSlideListItem = (SlideListItem) arguments.getSerializable(SlideListItem.INTENT_NAME);
    }

    public static SlideFragment createFromSlideListItem(@NonNull final SlideListItem slideListItem) {
        SlideFragment fragment = slideListItem.getSlideType().getFragmentInstance();

        // Create the detail fragment and add it to the activity
        // using a fragment transaction.
        Bundle arguments = new Bundle();
        arguments.putSerializable(SlideListItem.INTENT_NAME, slideListItem);

        fragment.setArguments(arguments);
        return fragment;
    }
}
