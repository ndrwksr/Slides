package com.ndrwksr.slideslib;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * A utility class for common operations involving strings.
 */
public class Strings {
    /**
     * Returns true if the provided string is null/empty, and false otherwise.
     *
     * @param string The string to check.
     * @return true if the provided string is null/empty, and false otherwise.
     */
    public static boolean isEmpty(@Nullable final String string) {
        return string == null || string.isEmpty();
    }

    /**
     * A utility method used to make error messages clearer by replacing missing strings with tokens
     * If the string was null, "null" is returned, else if the string was empty, "empty" is
     * returned, else the provided string is returned.
     * @param string The string to clarify.
     * @return a clarifying token if the string was empty, else the provided string.
     */
    @NonNull
    public static String clarifyStringIfMissing(@Nullable final String string) {
        final String retVal;
        if (string == null) {
            retVal = "<null>";
        } else if (string.isEmpty()) {
            retVal = "<empty>";
        } else {
            retVal = string;
        }
        return retVal;
    }

    /**
     * Reads the stream to the end, and returns the resulting string.
     * @param is The input stream to read from.
     * @return the string in the provided stream.
     * @throws IOException If there was an error reading from the stream.
     */
    public static String getStringFromStream(@NonNull final InputStream is) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }
}
