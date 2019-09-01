package com.ndrwksr.slideslib.slides;

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

import com.ndrwksr.slideslib.R;
import com.ndrwksr.slideslib.Slide;
import com.ndrwksr.slideslib.SlideFragment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A {@link Slide} which has static text at the top, and an editable text field on the bottom. The
 * text entered into the editable text field is persisted to a key/value store with the key in
 * {@link DocSlide#persistedDataKey}.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DocSlide extends Slide {
    /**
     * The key for this type of slide in type token maps.
     */
    public static final String TYPE_STRING = "doc";

    /**
     * The key used for persisting the text entered into the EditText view.
     */
    @NonNull
    private final String persistedDataKey;

    /**
     * All-args constructor (required for Lombok).
     *
     * @param slideId          The ID of the slide.
     * @param slideTitle       The title of the slide.
     * @param slideContent     The content of the slide.
     * @param persistedDataKey The key used for persisting the text entered into the EditText view.
     */
    public DocSlide(
            @NonNull final String slideId,
            @NonNull final String slideTitle,
            @NonNull final String slideContent,
            @NonNull final String persistedDataKey
    ) {
        super(slideId, slideTitle, slideContent);
        this.persistedDataKey = persistedDataKey;
    }

    @Override
    protected SlideFragment instantiateFragment() {
        return new DocSlideFragment();
    }

    /**
     * The {@link android.support.v4.app.Fragment} for {@link DocSlide}.
     */
    @NoArgsConstructor
    public static class DocSlideFragment extends SlideFragment<DocSlide> {
        /**
         * The text view for displaying the slide's content.
         */
        private TextView mTextView;

        /**
         * The edit text view that the user can put text into. The text entered into this field is
         * persisted with a key/value store with the key being {@link DocSlide#persistedDataKey}.
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

            mTextView.setText(mSlide.getContent());

            // TODO: Refactor persistence
            final String slideUserDataPref = mSlide.getPersistedDataKey();
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

}
