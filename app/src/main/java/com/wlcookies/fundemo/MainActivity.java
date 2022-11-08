package com.wlcookies.fundemo;


import android.os.Bundle;

import com.wlcookies.commonmodule.utils.PackageManagerUtils;
import com.wlcookies.fundemo.databinding.ActivityMainBinding;

/**
 * Main UI
 * <p>功能详细描述</p>
 *
 * @author weiguo
 * @version 1.0
 */
public class MainActivity extends BaseActivity {


    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PackageManagerUtils.test(getPackageManager());

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityMainBinding = null;
    }

}