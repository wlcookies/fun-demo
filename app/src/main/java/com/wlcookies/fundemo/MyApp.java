package com.wlcookies.fundemo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ProcessLifecycleInitializer;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.kaolafm.sdk.client.IServiceConnection;
import com.kaolafm.sdk.client.KLClientAPI;
import com.wlcookies.commonmodule.utils.LogUtils;
import com.wlcookies.fundemo.utils.ToastUtils;

public class MyApp extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        LogUtils.isDebug = true;

        // 云听SDK
//        KLClientAPI.getInstance().init(this, KLClientAPI.KEY_AUTO, new IServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName componentName) {
//                LogUtils.d("初始化成功 与云听 app 侧建立连接");
//                KLClientAPI.getInstance().play();
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName componentName) {
//            }
//        });
    }

    public static Context getContext() {
        return mContext;
    }
}
