package com.wlcookies.commonmodule.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * 包管理器工具
 */
public class PackageManagerUtils {
    private PackageManagerUtils() {
    }

    public static void test(PackageManager pm) {
        List<ApplicationInfo> installedApplications = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo installedApplication : installedApplications) {
//            LogUtils.d(installedApplication.packageName );
        }
    }
}