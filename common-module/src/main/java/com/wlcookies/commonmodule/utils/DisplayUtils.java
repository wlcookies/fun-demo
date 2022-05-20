package com.wlcookies.commonmodule.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.NonNull;


public class DisplayUtils {

    private DisplayUtils() {
    }

    private static final String TAG = DisplayUtils.class.getSimpleName();

    /**
     * Value of dp to value of px.
     *
     * @param dpValue The value of dp.
     * @return value of px
     */
    public static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Value of px to value of dp.
     *
     * @param pxValue The value of px.
     * @return value of dp
     */
    public static int px2dp(final float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * Value of sp to value of px.
     *
     * @param spValue The value of sp.
     * @return value of px
     */
    public static int sp2px(final float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * Value of px to value of sp.
     *
     * @param pxValue The value of px.
     * @return value of sp
     */
    public static int px2sp(final float pxValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 打印设备屏幕信息
     * <p>
     * 使用<b>adb shell wm [size | density]</b>
     * </p>
     * <ul>
     *     <li>1.width 屏幕宽度-px</li>
     *     <li>2.height 屏幕高度-px</li>
     *     <li>3.density 屏幕密度 (densityDpi/160)</li>
     *     <li>4.densityDpi 屏幕DPI</li>
     * </ul>
     *
     * @param activity activity
     */
    public static void printDisplayInfo(@NonNull Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;//屏幕宽度（单位：px）
        int height = metric.heightPixels;//屏幕高度（单位：px）
        float density = metric.density;//屏幕密度（常见的有：1.5、2.0、3.0）
        int densityDpi = metric.densityDpi;//屏幕DPI（常见的有：240、320、480）
        Log.d(TAG, "width =" + width + ",height =" + height + ",density =" + density + ",densityDpi ="
                + densityDpi);
    }
}