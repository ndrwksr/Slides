package com.example.slides.slideFragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.slides.R;

public class DocSlideFragment extends SlideFragment {

    private TextView mTextView;

    /**
     * The edit text view for displaying the slide's contents.
     */
    private EditText mEditText;

    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState
    ) {
        final View rootView = inflater.inflate(R.layout.slide_doc_view, container, false);
        mTextView = rootView.findViewById(R.id.slide_doc_title_text);
        mEditText = rootView.findViewById(R.id.slide_doc_edit_text);

        mTextView.setText(mSlideListItem.getSlideContent());


        final String slideUserDataPref = mSlideListItem.getSlideUserDataPref();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(inflater.getContext());
        final String editTextContent = preferences.getString(slideUserDataPref, "");
        mEditText.setText(editTextContent);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    preferences.edit().putString(slideUserDataPref, s.toString()).apply();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
    }
}
