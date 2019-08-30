package com.ndrwksr.slideslib;

import com.google.gson.reflect.TypeToken;
import com.ndrwksr.slideslib.exceptions.BadSlideDataException;
import com.ndrwksr.slideslib.exceptions.BadSlideTypeException;
import com.ndrwksr.slideslib.slides.TextSlide;

import org.junit.Test;

import java.util.Map;

@SuppressWarnings("ConstantConditions")
public class SlideSkeletonTest {
    private static final String CONTENT_TEST = "{\"content\":\"test\"}";

    @Test(expected = NullPointerException.class)
    public void toSlide_nullMap() throws BadSlideDataException, BadSlideTypeException {
        SlideSkeleton skeleton = new SlideSkeleton("text", CONTENT_TEST);
        skeleton.toSlide(null);
    }

    @Test(expected = BadSlideTypeException.class)
    public void toSlide_skeletonWithoutType() throws BadSlideDataException, BadSlideTypeException {
        Map<String, TypeToken<? extends Slide>> typeTokenMap = SlideLoader.makeDefaultTokenMap();
        SlideSkeleton skeleton = new SlideSkeleton("", CONTENT_TEST);
        skeleton.toSlide(typeTokenMap);
    }


    @Test(expected = BadSlideTypeException.class)
    public void toSlide_skeletonTypeNotInMap() throws BadSlideDataException, BadSlideTypeException {
        Map<String, TypeToken<? extends Slide>> typeTokenMap = SlideLoader.makeDefaultTokenMap();
        SlideSkeleton skeleton = new SlideSkeleton("'DROP TABLE USERS", CONTENT_TEST);
        skeleton.toSlide(typeTokenMap);
    }

    @Test(expected = BadSlideDataException.class)
    public void toSlide_skeletonWithNullData() throws BadSlideDataException, BadSlideTypeException {
        Map<String, TypeToken<? extends Slide>> typeTokenMap = SlideLoader.makeDefaultTokenMap();
        SlideSkeleton skeleton = new SlideSkeleton("text", null);
        skeleton.toSlide(typeTokenMap);
    }

    @Test(expected = BadSlideDataException.class)
    public void toSlide_skeletonWithEmptyData() throws BadSlideDataException, BadSlideTypeException {
        Map<String, TypeToken<? extends Slide>> typeTokenMap = SlideLoader.makeDefaultTokenMap();
        SlideSkeleton skeleton = new SlideSkeleton("text", "");
        skeleton.toSlide(typeTokenMap);
    }


    @Test(expected = BadSlideDataException.class)
    public void toSlide_skeletonWithBadData() throws BadSlideDataException, BadSlideTypeException {
        Map<String, TypeToken<? extends Slide>> typeTokenMap = SlideLoader.makeDefaultTokenMap();
        SlideSkeleton skeleton = new SlideSkeleton("text", "{]]This is!some@awful,,JSON\"");
        skeleton.toSlide(typeTokenMap);
    }

    @Test
    public void toSlide_validArguments() throws BadSlideDataException, BadSlideTypeException {
        Map<String, TypeToken<? extends Slide>> typeTokenMap = SlideLoader.makeDefaultTokenMap();
        SlideSkeleton skeleton = new SlideSkeleton("text", CONTENT_TEST);
        Slide slide = skeleton.toSlide(typeTokenMap);
        assert slide instanceof TextSlide;
        assert slide.getContent().equals("test");
    }
}
