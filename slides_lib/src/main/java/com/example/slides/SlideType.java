package com.example.slides;

import android.support.annotation.NonNull;

import com.example.slides.slideFragments.DocSlideFragment;
import com.example.slides.slideFragments.HtmlSlideFragment;
import com.example.slides.slideFragments.SlideFragment;
import com.example.slides.slideFragments.TextSlideFragment;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SlideType {

    @SerializedName("text") TEXT(TextSlideFragment.class),
    @SerializedName("html") HTML(HtmlSlideFragment.class),
    @SerializedName("doc") DOC(DocSlideFragment.class);

    @NonNull
    private final Class<? extends SlideFragment> fragmentClass;

    public SlideFragment getFragmentInstance() {
        if (this == TEXT) {
            return new TextSlideFragment();
        } else if (this == HTML) {
            return new HtmlSlideFragment();
        } else {
            return new DocSlideFragment();
        }
    }
}
