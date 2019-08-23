package com.ndrwksr.slideslib.slides;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.ndrwksr.slideslib.R;
import com.ndrwksr.slideslib.Slide;
import com.ndrwksr.slideslib.SlideFragment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
public class HtmlSlide extends Slide {
    public static final String TYPE_STRING = "html";

    public HtmlSlide(
            @NonNull final String slideId,
            @NonNull final String slideTitle,
            @NonNull final String slideContent
    ) {
        super(slideId, slideTitle, slideContent);
    }

    @Override
    protected SlideFragment makeFragment() {
        return new HtmlSlideFragment();
    }

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

            final String content = mSlide.getContent();
            final String encodedHtml = Base64.encodeToString(content.getBytes(), Base64.NO_PADDING);
            mWebView.loadData(encodedHtml, "text/html", "base64");

            return rootView;
        }
    }
}