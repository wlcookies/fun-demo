package com.wlcookies.commonmodule.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

/**
 * PermissionUtils
 * <p>
 * <b>Check permission: </b> {@link #hasPermission(Context, String)}, {@link #hasPermissions(Context, String[])}
 *
 * @author wg
 * @version 1.0
 * @see Manifest.permission
 */
public class PermissionUtils {

    private PermissionUtils() {
    }

    /**
     * Check single permission
     *
     * @param context    context
     * @param permission 权限字符串
     * @return true GRANTED, false DENIED
     */
    public static boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Check permissions group
     * <p>
     *
     * @param context     context
     * @param permissions permissions group
     * @return true GRANTED, false DENIED
     */
    public static boolean hasPermissions(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (!hasPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }
}
