package com.example.slides.slideFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.slides.R;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HtmlSlideFragment extends SlideFragment {

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

        final String content = mSlideListItem.getSlideContent();
        final String encodedHtml = Base64.encodeToString(content.getBytes(), Base64.NO_PADDING);
        mWebView.loadData(encodedHtml, "text/html", "base64");
        Log.i("Content:", content);
//        mWebView.loadUrl(mContent);

        return rootView;
    }
}
