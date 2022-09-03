package com.wlcookies.fundemo.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import com.wlcookies.commonmodule.utils.LogUtils;
import com.wlcookies.fundemo.BaseActivity;
import com.wlcookies.fundemo.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class TestActivity extends BaseActivity {

    private Handler mHandler;
    private SimpleDateFormat mSimpleDateFormat;
    private Runnable mRunnable;

    public static Intent newInstance(Context context) {
        return new Intent(context, TestActivity.class);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        findViewById(R.id.add_bt).setOnClickListener(v -> {

        });
        findViewById(R.id.remove_bt).setOnClickListener(v -> {

        });

    }
}