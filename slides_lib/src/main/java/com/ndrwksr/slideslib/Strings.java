package com.ndrwksr.slideslib;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Strings {
    // return true if string is null or empty
    public static boolean isEmpty(@Nullable final String string) {
        return string == null || string.isEmpty();
    }

    // Used to make error messages clearer by replacing missing strings with tokens
    // If string is null, returns "<null>",
    // If empty, returns "<empty>"
    // Else returns input
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
}
