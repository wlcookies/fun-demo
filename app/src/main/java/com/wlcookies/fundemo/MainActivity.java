package com.wlcookies.fundemo;


import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.wlcookies.commonmodule.utils.PackageManagerUtils;
import com.wlcookies.fundemo.databinding.ActivityMainBinding;
import com.wlcookies.fundemo.ui.DemoFragment;
import com.wlcookies.fundemo.ui.ToolFragment;
import com.wlcookies.fundemo.ui.WidgetFragment;

public class MainActivity extends BaseActivity implements NavigationBarView.OnItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PackageManagerUtils.test(getPackageManager());

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        initData();
    }

    private void initData() {
        activityMainBinding.mainBottomNavView.setOnItemSelectedListener(this);
        activityMainBinding.mainBottomNavView.setSelectedItemId(R.id.menu_widgets);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (item.getItemId()) {
            case R.id.menu_widgets:
                ft.replace(R.id.container_fl, WidgetFragment.newInstance()).commit();
                return true;
            case R.id.menu_tool:
                ft.replace(R.id.container_fl, ToolFragment.newInstance()).commit();
                return true;
            case R.id.menu_demo:
                ft.replace(R.id.container_fl, DemoFragment.newInstance()).commit();
                return true;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityMainBinding = null;
    }
}