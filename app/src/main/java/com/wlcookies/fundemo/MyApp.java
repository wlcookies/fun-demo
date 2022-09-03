package com.wlcookies.fundemo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.wlcookies.commonmodule.utils.LogUtils;

public class MyApp extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        LogUtils.isDebug = true;

    }

    public static Context getContext() {
        return mContext;
    }
}
