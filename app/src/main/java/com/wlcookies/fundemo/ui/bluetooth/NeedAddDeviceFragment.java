package com.wlcookies.fundemo.ui.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.wlcookies.fundemo.R;


/**
 * 添加连接设备
 * 在没有蓝牙设备连接时显示的页面
 */
public class NeedAddDeviceFragment extends Fragment {

    public static NeedAddDeviceFragment newInstance() {
        return new NeedAddDeviceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_need_add_device, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutCompat addDeviceLl = view.findViewById(R.id.add_device_ll);
        addDeviceLl.setOnClickListener(v -> {
            try {
                startActivity(new Intent("bmi.intent.action.BT_SETTINGS"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}