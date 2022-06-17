package com.wlcookies.commonmodule.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

/**
 * 提示（吐司）
 */
public class ShowMsgUtils {

    private ShowMsgUtils() {
    }

    /**
     * show 吐司
     *
     * @param context 上下文
     * @param msg     消息文本
     */
    public static void toast(Context context, @NonNull String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * show 吐司
     *
     * @param context 上下文
     * @param msg     消息文本资源
     */
    public static void toast(Context context, @StringRes int msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
