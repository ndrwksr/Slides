package com.ndrwksr.slideslib;

import com.google.gson.reflect.TypeToken;
import com.ndrwksr.slideslib.exceptions.BadSlideDataException;
import com.ndrwksr.slideslib.exceptions.BadSlideTypeException;
import com.ndrwksr.slideslib.exceptions.BadSlidesException;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;

@SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
public class SlideLoaderTest {
    // Calling SlideLoader.loadSlides calls read(char[], int, int) on the ISR exactly 3 times.
    private static final int LOAD_SLIDES_READ_INVOCATIONS = 3;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test(expected = NullPointerException.class)
    public void registerSlideTypes_nullMap() throws BadSlideTypeException {
        new SlideLoader().registerSlideTypes(null);
    }

    @Test(expected = BadSlideTypeException.class)
    public void registerSlideTypes_nullKey() throws BadSlideTypeException {
        Map<String, TypeToken<? extends Slide>> typeTokenMap = new HashMap<>();
        typeTokenMap.put(null, null);
        new SlideLoader().registerSlideTypes(typeTokenMap);
    }

    @Test(expected = BadSlideTypeException.class)
    public void registerSlideTypes_emptyKey() throws BadSlideTypeException {
        Map<String, TypeToken<? extends Slide>> typeTokenMap = new HashMap<>();
        typeTokenMap.put("", null);
        new SlideLoader().registerSlideTypes(typeTokenMap);
    }

    @Test(expected = BadSlideTypeException.class)
    public void registerSlideTypes_emptyValue() throws BadSlideTypeException {
        Map<String, TypeToken<? extends Slide>> typeTokenMap = new HashMap<>();
        typeTokenMap.put("key", null);
        new SlideLoader().registerSlideTypes(typeTokenMap);
    }

    @Test
    public void registerSlideTypes_goodToken() throws BadSlideTypeException {
        final String TEST_KEY = "test_key";
        Map<String, TypeToken<? extends Slide>> typeTokenMap = new HashMap<>();
        typeTokenMap.put(TEST_KEY, new TypeToken<Slide>() {
        });

        SlideLoader slideLoader = new SlideLoader();
        slideLoader.registerSlideTypes(typeTokenMap);
        assert slideLoader.getTypeTokenMap().containsKey(TEST_KEY);
    }

    @Test(expected = NullPointerException.class)
    public void getSlides_nullReader() throws BadSlidesException {
        new SlideLoader().getSlides(null);
    }

    @Test
    public void getSlides_slidesHaveNotBeenLoaded() throws IOException, BadSlidesException {
        SlideLoader slideLoader = new SlideLoader();
        InputStreamReader realIsr = new InputStreamReader(
                new ByteArrayInputStream("[]".getBytes())
        );
        InputStreamReader spyIsr = Mockito.spy(realIsr);
        slideLoader.getSlides(spyIsr);
        verify(spyIsr, Mockito.times(LOAD_SLIDES_READ_INVOCATIONS)).read(
                ArgumentMatchers.<char[]>any(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt()
        );
    }

    @Test
    public void getSlides_slidesWereAlreadyLoaded() throws IOException, BadSlidesException {
        SlideLoader slideLoader = new SlideLoader();
        InputStreamReader realIsr = new InputStreamReader(
                new ByteArrayInputStream("[]".getBytes())
        );
        InputStreamReader spyIsr = Mockito.spy(realIsr);
        slideLoader.getSlides(spyIsr);
        slideLoader.getSlides(spyIsr);
        verify(spyIsr, Mockito.times(LOAD_SLIDES_READ_INVOCATIONS)).read(
                ArgumentMatchers.<char[]>any(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt()
        );
    }

    @Test(expected = NullPointerException.class)
    public void reloadSlides_nullReader() throws BadSlidesException {
        new SlideLoader().reloadSlides(null);
    }

    @Test
    public void reloadSlides_validReader() throws IOException, BadSlidesException {
        SlideLoader slideLoader = new SlideLoader();
        InputStreamReader realIsr = new InputStreamReader(
                new ByteArrayInputStream("[]".getBytes())
        );
        InputStreamReader spyIsr = Mockito.spy(realIsr);
        slideLoader.reloadSlides(spyIsr);
        verify(spyIsr, Mockito.times(LOAD_SLIDES_READ_INVOCATIONS)).read(
                ArgumentMatchers.<char[]>any(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt()
        );
    }

    @Test(expected = BadSlidesException.class)
    public void loadSlides_badData() throws BadSlidesException {
        SlideLoader slideLoader = new SlideLoader();
        InputStreamReader realIsr = new InputStreamReader(
                new ByteArrayInputStream("I'm a teapot.".getBytes())
        );
        InputStreamReader spyIsr = Mockito.spy(realIsr);
        slideLoader.getSlides(spyIsr);
    }

    @Test
    public void loadSlides_nullSkeleton() throws BadSlidesException {
        SlideLoader slideLoader = new SlideLoader();
        InputStreamReader realIsr = new InputStreamReader(new ByteArrayInputStream((
                "[null]"
        ).getBytes()));
        InputStreamReader spyIsr = Mockito.spy(realIsr);
        slideLoader.getSlides(spyIsr);
        List<Exception> loadExceptions = slideLoader.getLoadExceptions();
        assert loadExceptions.size() == 1;
        assert loadExceptions.get(0) instanceof BadSlideDataException;
    }

    @Test
    public void loadSlides_skeletonHasBadType() throws BadSlidesException {
        SlideLoader slideLoader = new SlideLoader();
        InputStreamReader realIsr = new InputStreamReader(new ByteArrayInputStream((
                "[{\"type\":\"\",\"data\":\"{\\\"id\\\":\\\"first_slide\\\",\\\"title\\\":\\\"First Slide\\\",\\\"content\\\":\\\"This is some text for the first slide\\\"}\"}]"
        ).getBytes()));
        InputStreamReader spyIsr = Mockito.spy(realIsr);
        slideLoader.getSlides(spyIsr);
        List<Exception> loadExceptions = slideLoader.getLoadExceptions();
        assert loadExceptions.size() == 1;
        assert loadExceptions.get(0) instanceof BadSlideTypeException;
    }

    @Test
    public void loadSlides_skeletonHasBadData() throws BadSlidesException {
        SlideLoader slideLoader = new SlideLoader();
        InputStreamReader realIsr = new InputStreamReader(new ByteArrayInputStream((
                "[{\"type\":\"text\",\"data\":\"You must return here with a shrubbery\"}]"
        ).getBytes()));
        InputStreamReader spyIsr = Mockito.spy(realIsr);
        slideLoader.getSlides(spyIsr);
        List<Exception> loadExceptions = slideLoader.getLoadExceptions();
        assert loadExceptions.size() == 1;
        assert loadExceptions.get(0) instanceof BadSlideDataException;
    }

    @Test
    public void loadSlides_validData() throws BadSlidesException {
        SlideLoader slideLoader = new SlideLoader();
        InputStreamReader realIsr = new InputStreamReader(new ByteArrayInputStream((
                "[{\"type\":\"text\",\"data\":\"{\\\"id\\\":\\\"first_slide\\\",\\\"title\\\":\\\"First Slide\\\",\\\"content\\\":\\\"This is some text for the first slide\\\"}\"}]"
        ).getBytes()));
        InputStreamReader spyIsr = Mockito.spy(realIsr);
        slideLoader.getSlides(spyIsr);
        assert slideLoader.getLoadExceptions().size() == 0;
    }
}