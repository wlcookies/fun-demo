package com.wlcookies.commonmodule.utils;

public class SafetyUtils {

    /**
     * 获取安全的String
     *
     * @param str 字符串对象
     * @return string
     */
    public static String getString(String str) {
        if (str == null) {
            return "";
        } else {
            return str;
        }
    }

}
