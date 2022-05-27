package com.wlcookies.fundemo;

import static com.wlcookies.mediasessionmodule.MediaClient.log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.wlcookies.fundemo.ui.DemoFragment;
import com.wlcookies.fundemo.ui.ToolFragment;
import com.wlcookies.fundemo.ui.WidgetFragment;
import com.wlcookies.mediasessionmodule.MediaClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements NavigationBarView.OnItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private BottomNavigationView mBottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();


        List<PackageInfo> installedPackages = getPackageManager().getInstalledPackages(0);
        for (PackageInfo installedPackage : installedPackages) {
            log(installedPackage.packageName);
        }
    }

    private void initData() {
        mBottomNav = findViewById(R.id.main_bottom_nav_view);
        mBottomNav.setOnItemSelectedListener(this);
        mBottomNav.setSelectedItemId(R.id.menu_widgets);
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
}