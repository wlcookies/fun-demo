package com.wlcookies.commonmodule.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class ServiceUtils {

    private ServiceUtils() {
    }

    /**
     * 判断服务是否在运行
     *
     * @param context
     * @param className 　　Service.class.getName();
     * @return
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> seviceList = activityManager.getRunningServices(200);
        if (seviceList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < seviceList.size(); i++) {
            if (seviceList.get(i).service.getClassName().toString().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
