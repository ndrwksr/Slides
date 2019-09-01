package com.ndrwksr.slideslib.slides;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.ndrwksr.slideslib.R;
import com.ndrwksr.slideslib.Slide;
import com.ndrwksr.slideslib.SlideFragment;
import com.ndrwksr.slideslib.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A {@link Slide} for displaying the contents of HTML files.
 *
 * The content of this slide must be the name of the target HTML file, such as:
 *   {
 *     "type":"htmlfile",
 *     "data":"{..., \"content\":\"[name of file].html\"}"
 *   },
 *
 * The HTML file for a slide must be located in the assets directory, under the following path:
 * [assets dir]/web/[{@link Slide#content}]_files/[{@link Slide#content}]
 *
 * The base URL for this page will be [assets dir]/web/.
 *
 * See the example app for an example usage of this slide type.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HtmlFileSlide extends Slide {
    /**
     * The key for this type of slide in type token maps.
     */
    public static final String TYPE_STRING = "htmlfile";

    /**
     * All-args constructor (required for Lombok).
     *
     * @param slideId      The ID of the slide.
     * @param slideTitle   The title of the slide.
     * @param slideContent The content of the slide.
     */
    public HtmlFileSlide(
            @NonNull final String slideId,
            @NonNull final String slideTitle,
            @NonNull final String slideContent
    ) {
        super(slideId, slideTitle, slideContent);
    }

    @Override
    protected SlideFragment instantiateFragment() {
        return new HtmlFileSlideFragment();
    }

    /**
     * The {@link android.support.v4.app.Fragment} for {@link HtmlFileSlide}.
     */
    @NoArgsConstructor
    public static class HtmlFileSlideFragment extends SlideFragment {
        /**
         * Log tag for errors encountered when loading HTML files.
         */
        public static final String HTML_FILE = "HTML_FILE";

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
            final String htmlFileBaseUrl = "file:///android_asset/web/";
            final String htmlFileDir = content.replaceAll("\\.html", "") + "_files";

            final String html;
            try {
                final InputStream is = inflater.getContext()
                        .getAssets()
                        .open("web/" + htmlFileDir + "/" + content);
                html = Strings.getStringFromStream(is);

                mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
                mWebView.loadDataWithBaseURL(
                        htmlFileBaseUrl + htmlFileDir,
                        html,
                        "text/html",
                        StandardCharsets.UTF_8.name(),
                        null
                );
            } catch (IOException e) {
                Log.e(HTML_FILE, "Couldn't get HTML for and HTML file slide.", e);
            }

            return rootView;
        }
    }

}