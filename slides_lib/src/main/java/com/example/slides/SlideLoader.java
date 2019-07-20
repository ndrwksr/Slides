package com.example.slides;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

@Getter
public class SlideLoader {
    private final List<SlideListItem> slidesList = new ArrayList<>();

    public SlideLoader(@NonNull final Context context) {
        try (InputStream is = context.getAssets().open("slides.json")) {
            Gson gson = new Gson();
            SlideListItem[] slidesArray = gson.fromJson(new InputStreamReader(is), SlideListItem[].class);
            Collections.addAll(slidesList, slidesArray);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
