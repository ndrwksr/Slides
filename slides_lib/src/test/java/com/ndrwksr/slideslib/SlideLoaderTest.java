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
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;

@SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
public class SlideLoaderTest {
    // Calling SlideLoader.loadSlides calls read(char[], int, int) on the stream exactly 2 times.
    private static final int LOAD_SLIDES_READ_INVOCATIONS = 2;

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
        InputStream realIs = new ByteArrayInputStream("[]".getBytes());
        InputStream spyIs = Mockito.spy(realIs);
        slideLoader.getSlides(spyIs);
        verify(spyIs, Mockito.times(LOAD_SLIDES_READ_INVOCATIONS)).read(
                ArgumentMatchers.<byte[]>any(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt()
        );
    }

    @Test
    public void getSlides_slidesWereAlreadyLoaded() throws IOException, BadSlidesException {
        SlideLoader slideLoader = new SlideLoader();
        InputStream firstRealIs = new ByteArrayInputStream("[]".getBytes());
        InputStream firstSpyIs = Mockito.spy(firstRealIs);
        InputStream secondRealIs = new ByteArrayInputStream("[]".getBytes());
        InputStream secondSpyIs = Mockito.spy(secondRealIs);
        slideLoader.getSlides(firstSpyIs);
        slideLoader.getSlides(secondSpyIs);
        verify(secondSpyIs, Mockito.times(LOAD_SLIDES_READ_INVOCATIONS)).read(
                ArgumentMatchers.<byte[]>any(),
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
        InputStream realIs = new ByteArrayInputStream("[]".getBytes());
        InputStream spyIs = Mockito.spy(realIs);
        slideLoader.reloadSlides(spyIs);
        verify(spyIs, Mockito.times(LOAD_SLIDES_READ_INVOCATIONS)).read(
                ArgumentMatchers.<byte[]>any(),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.anyInt()
        );
    }

    @Test(expected = BadSlidesException.class)
    public void loadSlides_badData() throws BadSlidesException {
        SlideLoader slideLoader = new SlideLoader();
        InputStream realIs = new ByteArrayInputStream("I'm a teapot.".getBytes());
        InputStream spyIs = Mockito.spy(realIs);
        slideLoader.getSlides(spyIs);
    }

    @Test
    public void loadSlides_nullSkeleton() throws BadSlidesException {
        SlideLoader slideLoader = new SlideLoader();
        InputStream realIs = new ByteArrayInputStream("[null]".getBytes());
        InputStream spyIs = Mockito.spy(realIs);
        slideLoader.getSlides(spyIs);
        List<Exception> loadExceptions = slideLoader.getParseExceptions();
        assert loadExceptions.size() == 1;
        assert loadExceptions.get(0) instanceof BadSlideDataException;
    }

    @Test
    public void loadSlides_skeletonHasBadType() throws BadSlidesException {
        SlideLoader slideLoader = new SlideLoader();
        InputStream realIs = new ByteArrayInputStream((
                "[{\"type\":\"\",\"data\":\"{\\\"id\\\":\\\"first_slide\\\",\\\"title\\\":\\\"First Slide\\\",\\\"content\\\":\\\"This is some text for the first slide\\\"}\"}]"
        ).getBytes());
        InputStream spyIs = Mockito.spy(realIs);
        slideLoader.getSlides(spyIs);
        List<Exception> loadExceptions = slideLoader.getParseExceptions();
        assert loadExceptions.size() == 1;
        assert loadExceptions.get(0) instanceof BadSlideTypeException;
    }

    @Test
    public void loadSlides_skeletonHasBadData() throws BadSlidesException {
        SlideLoader slideLoader = new SlideLoader();
        InputStream realIs = new ByteArrayInputStream((
                "[{\"type\":\"text\",\"data\":\"You must return here with a shrubbery\"}]"
        ).getBytes());
        InputStream spyIs = Mockito.spy(realIs);
        slideLoader.getSlides(spyIs);
        List<Exception> loadExceptions = slideLoader.getParseExceptions();
        assert loadExceptions.size() == 1;
        assert loadExceptions.get(0) instanceof BadSlideDataException;
    }

    @Test
    public void loadSlides_validData() throws BadSlidesException {
        SlideLoader slideLoader = new SlideLoader();
        InputStream realIs = new ByteArrayInputStream((
                "[{\"type\":\"text\",\"data\":\"{\\\"id\\\":\\\"first_slide\\\",\\\"title\\\":\\\"First Slide\\\",\\\"content\\\":\\\"This is some text for the first slide\\\"}\"}]"
        ).getBytes());
        InputStream spyIs = Mockito.spy(realIs);
        slideLoader.getSlides(spyIs);
        assert slideLoader.getParseExceptions().size() == 0;
    }
}