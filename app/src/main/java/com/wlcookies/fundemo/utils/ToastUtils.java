package com.wlcookies.fundemo.utils;

import android.widget.Toast;

import com.wlcookies.fundemo.MyApp;

public class ToastUtils {
    public static void showMsg(String msg) {
        Toast.makeText(MyApp.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}