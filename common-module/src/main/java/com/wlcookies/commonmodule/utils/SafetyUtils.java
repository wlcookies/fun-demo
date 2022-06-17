package com.wlcookies.commonmodule.utils;

import androidx.annotation.NonNull;

public class SafetyUtils {

    private SafetyUtils() {
    }

    /**
     * 获取安全的String
     *
     * @param str 字符串对象
     * @return string
     */
    public static @NonNull
    String getString(String str) {
        if (str == null) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 获取安全的String，null "" 返回 defaultStr
     *
     * @param str 字符串对象
     * @return string
     */
    public static @NonNull
    String getString(String str, @NonNull String defaultStr) {
        if (str == null || "".equals(str)) {
            return defaultStr;
        } else {
            return str;
        }
    }
}
