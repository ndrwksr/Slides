package com.example.slides;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

import lombok.Data;

@Data
public class SlideListItem implements Serializable {
    public static final String INTENT_NAME = "slide_list_item";
    @NonNull
    private final SlideType slideType;
    @NonNull
    private final String slideId;
    @NonNull
    private final String slideTitle;
    @NonNull
    private final String slideContent;
    @Nullable
    private final String slideUserDataPref;
}
