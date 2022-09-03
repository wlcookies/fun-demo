package com.wlcookies.fundemo.ui;

import android.app.ActivityOptions;
import android.app.UiModeManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioRouting;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.collection.ArrayMap;
import androidx.collection.LruCache;
import androidx.constraintlayout.widget.StateSet;
import androidx.fragment.app.Fragment;
import androidx.media.AudioFocusRequestCompat;

import com.wlcookies.commonmodule.utils.LogUtils;
import com.wlcookies.commonmodule.utils.ShowMsgUtils;
import com.wlcookies.fundemo.R;
import com.wlcookies.fundemo.databinding.FragmentWidgetBinding;

import java.util.List;

/**
 * 控件使用Demo
 *
 * @author wl
 * @version V1.0
 * @date 2022/5/12
 */
public class WidgetFragment extends Fragment implements SensorEventListener {

    private FragmentWidgetBinding fragmentWidgetBinding;
    private SensorManager mSensorManager;

    public static WidgetFragment newInstance() {
        return new WidgetFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentWidgetBinding = FragmentWidgetBinding.inflate(inflater, container, false);
        return fragmentWidgetBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentWidgetBinding.pkgBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = fragmentWidgetBinding.pkgEt.getText().toString();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    openApp(requireActivity(), text);
                }
            }
        });

        mSensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        for (Sensor sensor : sensorList) {
            LogUtils.d("传感器列表： " + sensor.getName() + " " + sensor.getVendor() + " " + sensor.getStringType());
        }

        Sensor defaultGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Sensor defaultAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor defaultLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (defaultGyroscopeSensor != null && defaultAccelerometerSensor != null && defaultLightSensor != null) {
            LogUtils.d("默认陀螺仪： " + defaultGyroscopeSensor);
            mSensorManager.registerListener(this, defaultGyroscopeSensor, SensorManager.SENSOR_DELAY_UI);
        }

        if (defaultAccelerometerSensor != null) {
            LogUtils.d("默认加速度： " + defaultAccelerometerSensor);
            mSensorManager.registerListener(this, defaultAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        }

        if (defaultLightSensor != null) {
            LogUtils.d("默认光照强度： " + defaultLightSensor);
            mSensorManager.registerListener(this, defaultLightSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentWidgetBinding = null;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        LogUtils.d("传感器回调-----------------开始");
        int type = event.sensor.getType();
        if (type == Sensor.TYPE_GYROSCOPE) {
            LogUtils.d("陀螺仪 GYROSCOPE " + event.values[0] + "," + event.values[1] + "," + event.values[2]);
        } else if (type == Sensor.TYPE_ACCELEROMETER) {
            LogUtils.d("加速度 ACCELEROMETER " + event.values[0] + "," + event.values[1] + "," + event.values[2]);
        } else if (type == Sensor.TYPE_LIGHT) {
            LogUtils.d("光照强度 LIGHT " + event.values[0]);
        }
        LogUtils.d("传感器回调-----------------结束");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void openApp(Context context, String pkgName) {
        try {
            Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(pkgName);
            if (launchIntentForPackage != null) {
                // 调用framework.jar的隐藏方法，报红正常
//                int displayId = context.getDisplayId();
//                ActivityOptions activityOptions = ActivityOptions.makeBasic();
//                activityOptions.setLaunchDisplayId(displayId);
                context.startActivity(launchIntentForPackage);
            } else {
                ShowMsgUtils.toast(context, "没有安装此APP");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ShowMsgUtils.toast(context, "打开APP失败");
        }
    }
}