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
        List<ApplicationInfo> installedApplications = pm.getInstalledApplications(0);
        for (ApplicationInfo installedApplication : installedApplications) {
            if ("res/cs.xml".equals(installedApplication.loadLabel(pm).toString())) {
                LogUtils.d(installedApplication.packageName + " === " + installedApplication.loadLabel(pm).toString());
            }
        }
    }
}