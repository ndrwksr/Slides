package com.ndrwksr.slides.slideFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.ndrwksr.slides.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HtmlFileSlideFragment extends SlideFragment {

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
        final String htmlFileBaseUrl = "file:///android_asset/web/";
        final String htmlFileDir = content.replaceAll("\\.html", "") + "_files";

        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = inflater.getContext().getAssets().open("web/" + htmlFileDir + "/" + content);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.loadDataWithBaseURL(htmlFileBaseUrl + htmlFileDir, sb.toString(), "text/html", "utf-8", null);

        return rootView;
    }
}
