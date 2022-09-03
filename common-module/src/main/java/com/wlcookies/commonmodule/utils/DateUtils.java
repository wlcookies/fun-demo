package com.wlcookies.commonmodule.utils;

import android.annotation.SuppressLint;

import java.util.Formatter;
import java.util.Locale;

/**
 * date methods
 */
public class DateUtils {

    private static final StringBuilder mFormatBuilder = new StringBuilder();
    @SuppressLint("ConstantLocale")
    private static final Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

    public static String hhmm(int timeMs) {
        try {
            if (timeMs <= 0) {
                return "00:00";
            }
            int totalSeconds = timeMs / 1000;
            int seconds = totalSeconds % 60;
            int minutes = (totalSeconds / 60) % 60;
            int hours = totalSeconds / 3600;

            mFormatBuilder.setLength(0);
            if (hours > 0) {
                return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
            } else {
                return mFormatter.format("%02d:%02d", minutes, seconds).toString();
            }
        } catch (Exception e) {
            return "00:00";
        }
    }

    /**
     * 时分秒转整型
     *
     * @param str
     * @return
     */
    private int wav(String str) {
        String str1[] = str.split(":");
        //0->时，1->分，2->秒
        int m = Integer.parseInt(str1[2]);
        int f = Integer.parseInt(str1[1]) * 60;
        int s = Integer.parseInt(str1[0]) * 60 * 60;
        int miao = (m + f + s) * 1000;
        return miao;
    }
}
