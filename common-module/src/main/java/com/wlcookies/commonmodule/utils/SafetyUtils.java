package com.wlcookies.commonmodule.utils;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * Get object values in a safe way
 * <p>
 * Contains the following methods:
 *     <ul>
 *         <li>{@link String} - {@link #getString(String)} {@link #getString(String, String)}</li>
 *     </ul>
 * </p>
 *
 * @author weiguo
 * @version 1.0
 */
public class SafetyUtils {

    private SafetyUtils() {
    }

    /**
     * get string in a safe way
     *
     * @param str String
     * @return String returns "" if str reference is null
     */
    @NonNull
    public static String getString(String str) {
        return Objects.isNull(str) ? "" : str;
    }

    /**
     * get string in a safe way
     *
     * @param str        String
     * @param defaultStr NonNull default String
     * @return String returns default string if str reference is null or is ""
     */
    @NonNull
    public static String getString(String str, @NonNull String defaultStr) {
        String result = getString(str);
        return "".equals(result) ? defaultStr : result;
    }


}
