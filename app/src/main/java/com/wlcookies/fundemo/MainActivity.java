package com.wlcookies.fundemo;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.wlcookies.fundemo.ui.DemoFragment;
import com.wlcookies.fundemo.ui.ToolFragment;
import com.wlcookies.fundemo.ui.WidgetFragment;

public class MainActivity extends BaseActivity implements NavigationBarView.OnItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private BottomNavigationView mBottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
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