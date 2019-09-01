package com.ndrwksr.slideslib.slides;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.ndrwksr.slideslib.R;
import com.ndrwksr.slideslib.Slide;
import com.ndrwksr.slideslib.SlideFragment;

import java.nio.charset.StandardCharsets;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A {@link Slide} for displaying HTML. The {@link Slide#content} field is used to provide the HTML.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HtmlSlide extends Slide {
    /**
     * The key for this type of slide in type token maps.
     */
    public static final String TYPE_STRING = "html";

    /**
     * All-args constructor (required for Lombok).
     * @param slideId The ID of the slide.
     * @param slideTitle The title of the slide.
     * @param slideContent The content of the slide.
     */
    public HtmlSlide(
            @NonNull final String slideId,
            @NonNull final String slideTitle,
            @NonNull final String slideContent
    ) {
        super(slideId, slideTitle, slideContent);
    }

    @Override
    protected SlideFragment instantiateFragment() {
        return new HtmlSlideFragment();
    }

    /**
     * The {@link android.support.v4.app.Fragment} for {@link HtmlSlide}.
     */
    @NoArgsConstructor
    public static class HtmlSlideFragment extends SlideFragment<HtmlSlide> {

        /**
         * The web view for displaying the slide's contents.
         */
        private WebView mWebView;

        @Override
        public View onCreateView(
                @NonNull final LayoutInflater inflater,
                @Nullable final ViewGroup container,
                @Nullable final Bundle savedInstanceState
        ) {
            final View rootView = inflater.inflate(R.layout.slide_web_view, container, false);
            mWebView = rootView.findViewById(R.id.slide_web_view);

            mWebView.loadData(mSlide.getContent(), "text/html", StandardCharsets.UTF_8.name());

            return rootView;
        }
    }
}