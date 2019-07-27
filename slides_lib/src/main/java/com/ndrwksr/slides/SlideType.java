package com.ndrwksr.slides;

import android.support.annotation.NonNull;

import com.ndrwksr.slides.slideFragments.DocSlideFragment;
import com.ndrwksr.slides.slideFragments.HtmlFileSlideFragment;
import com.ndrwksr.slides.slideFragments.HtmlSlideFragment;
import com.ndrwksr.slides.slideFragments.SlideFragment;
import com.ndrwksr.slides.slideFragments.TextSlideFragment;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SlideType {

    @SerializedName("text") TEXT(TextSlideFragment.class),
    @SerializedName("html") HTML(HtmlSlideFragment.class),
    @SerializedName("doc") DOC(DocSlideFragment.class),
    @SerializedName("htmlfile") HTMLFILE(HtmlFileSlideFragment.class);

    @NonNull
    private final Class<? extends SlideFragment> fragmentClass;

    @NonNull
    public SlideFragment getFragmentInstance() {
        if (this == TEXT) {
            return new TextSlideFragment();
        } else if (this == HTML) {
            return new HtmlSlideFragment();
        } else if (this == DOC){
            return new DocSlideFragment();
        } else {
            return new HtmlFileSlideFragment();
        }
    }
}
