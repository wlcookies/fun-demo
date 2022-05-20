package com.wlcookies.fundemo.ui.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.material.button.MaterialButton;
import com.wlcookies.fundemo.BaseActivity;
import com.wlcookies.fundemo.BuildConfig;
import com.wlcookies.fundemo.R;
import com.wlcookies.fundemo.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 蓝牙相关
 */
public class BluetoothActivity extends BaseActivity {

    private static final String TAG = "BluetoothActivity";

    private MaterialButton mOpenBt, mScanBt;
    private RecyclerView mDeviceRv;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    private boolean isScanning; // 是否在扫描
    private BluetoothLeScanner mBluetoothLeScanner;
    private List<MyBluetoothDevice> deviceList = new ArrayList<>();
    private BaseQuickAdapter<MyBluetoothDevice, BaseViewHolder> mDeviceAdapter;

    public static Intent newInstance(Context context) {
        return new Intent(context, BluetoothActivity.class);
    }

    private class MyBluetoothDevice {
        private String mac;
        private int rssi;

        public MyBluetoothDevice() {
        }

        public MyBluetoothDevice(String mac, int rssi) {
            this.mac = mac;
            this.rssi = rssi;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public int getRssi() {
            return rssi;
        }

        public void setRssi(int rssi) {
            this.rssi = rssi;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

        mOpenBt = findViewById(R.id.open_bt);
        mScanBt = findViewById(R.id.scan_bt);
        mDeviceRv = findViewById(R.id.device_rv);

        mDeviceRv.setLayoutManager(new LinearLayoutManager(this));
        mDeviceAdapter = new BaseQuickAdapter<MyBluetoothDevice, BaseViewHolder>(R.layout.bluetooth_device_item, deviceList) {

            @Override
            protected void convert(BaseViewHolder holder, MyBluetoothDevice myBluetoothDevice) {
                holder.setText(R.id.mac_tv, myBluetoothDevice.getMac());
                holder.setText(R.id.rssi_tv, "" + myBluetoothDevice.getRssi());
            }
        };
        mDeviceRv.setAdapter(mDeviceAdapter);

        ActivityResultLauncher<Intent> enableBluetoothLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                ToastUtils.showMsg(isOpenBluetooth() ? "蓝牙已打开" : "蓝牙未打开");
            }
        });
        ActivityResultLauncher<String[]> permissionLaunch = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            Collection<Boolean> values = result.values();
            if (values.contains(false)) {
                ToastUtils.showMsg("需要权限开启蓝牙功能");
            } else {
                enableBluetoothLaunch.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
            }
        });


        ActivityResultLauncher<Intent> testLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Log.d(TAG, "执行结果: --- " + result.getResultCode());
                if(result.getResultCode() == RESULT_OK){
                    Log.d(TAG, "执行结果: --- 成功" + result.getResultCode());
                }else if(result.getResultCode() == RESULT_CANCELED){
                    Log.d(TAG, "执行结果: --- 取消" + result.getResultCode());
                }
            }
        });

        // 打开蓝牙
        mOpenBt.setOnClickListener(v -> {
            // 判断蓝牙是否打开
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            testLauncher.launch(intent);

//            if (isOpenBluetooth()) {
//                ToastUtils.showMsg("蓝牙已经打开");
//                return;
//            }
//
//            if (!(hasPermission(Manifest.permission.BLUETOOTH) && hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) && hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION) && hasPermission(Manifest.permission.BLUETOOTH_SCAN))) {
//                permissionLaunch.launch(new String[]{
//                        Manifest.permission.BLUETOOTH,
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                });
//                return;
//            }
//            enableBluetoothLaunch.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        });

        ScanCallback scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                addDeviceList(new MyBluetoothDevice(
                        result.getDevice().getAddress(),
                        result.getRssi()
                ));
            }
        };
        // 开始扫描或者关闭扫描
        mScanBt.setOnClickListener(v -> {
            // 检查 bluetooth scan权限
            if (isOpenBluetooth()) {
                if (mBluetoothLeScanner != null) {
                    if (isScanning) {
                        isScanning = false;
                        mBluetoothLeScanner.stopScan(scanCallback);
                        mScanBt.setText(R.string.start_scan_bluetooth);
                        Log.d(TAG, "蓝牙设备数量： " + deviceList.size());
                    } else {
                        isScanning = true;
                        mBluetoothLeScanner.startScan(scanCallback);
                        mScanBt.setText(R.string.stop_start_bluetooth);
                    }
                }
            }
        });
    }

    /**
     * 判断蓝牙是否开启
     *
     * @return 是否开启
     */
    private boolean isOpenBluetooth() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter adapter = bluetoothManager.getAdapter();
        return adapter != null && adapter.isEnabled();
    }

    /**
     * 判断是否用此权限
     *
     * @param permission 需要判断的权限
     * @return boolean
     */
    private boolean hasPermission(String permission) {
        return getPackageManager().checkPermission(permission, BuildConfig.APPLICATION_ID) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 添加扫描设备
     */
    @SuppressLint("NotifyDataSetChanged")
    private void addDeviceList(MyBluetoothDevice myBluetoothDevice) {
        int deviceIndex = findDeviceIndex(myBluetoothDevice, deviceList);
        if (deviceIndex == -1) {
            deviceList.add(myBluetoothDevice);
        } else {
            deviceList.get(deviceIndex).setRssi(myBluetoothDevice.getRssi());
        }
        mDeviceAdapter.notifyDataSetChanged();
    }

    private int findDeviceIndex(MyBluetoothDevice scanDevice, List<MyBluetoothDevice> deviceList) {
        int index = 0;
        for (MyBluetoothDevice myBluetoothDevice : deviceList) {
            if (scanDevice.getMac().equals(myBluetoothDevice.getMac())) {
                return index;
            }
            index += 1;
        }
        return -1;
    }
}