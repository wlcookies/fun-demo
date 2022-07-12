package com.wlcookies.fundemo.ui.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.wlcookies.fundemo.BaseActivity;
import com.wlcookies.fundemo.R;

/**
 * 蓝牙音乐
 */
public class BluetoothMusicActivity extends BaseActivity {

    private A2dpConnectionStateReceiver mA2dpConnectionStateReceiver = new A2dpConnectionStateReceiver();

    public static Intent newInstance(Context context) {
        return new Intent(context, BluetoothMusicActivity.class);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_music);

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter != null) {
            // 主动判断 11 A2DP Sink Profile
            a2dpStateChange(bluetoothAdapter.getProfileConnectionState(11));
        } else {
            a2dpStateChange(BluetoothA2dp.STATE_DISCONNECTED);
        }
        // 注册广播判断
        registerA2dpConnectionStateReceiver(this);
    }

    public void registerA2dpConnectionStateReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        // A2DP Sink Profile 广播
        intentFilter.addAction("android.bluetooth.a2dp-sink.profile.action.CONNECTION_STATE_CHANGED");
        context.registerReceiver(mA2dpConnectionStateReceiver, intentFilter);
    }

    /**
     * A2DP sink端广播接收器
     */
    class A2dpConnectionStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if ("android.bluetooth.a2dp-sink.profile.action.CONNECTION_STATE_CHANGED".equals(action)) {
                    int a2dpState = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, BluetoothA2dp.STATE_DISCONNECTED);
                    a2dpStateChange(a2dpState);
                }
            } catch (Exception e) {
                e.printStackTrace();
                a2dpStateChange(BluetoothA2dp.STATE_DISCONNECTED);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销A2DP sink端广播
        unregisterReceiver(mA2dpConnectionStateReceiver);
        mA2dpConnectionStateReceiver = null;
    }

    /**
     * 根据状态切换界面
     *
     * @param a2dpState a2dp sink 端连接状态
     */
    public void a2dpStateChange(int a2dpState) {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (a2dpState == BluetoothA2dp.STATE_CONNECTED) {
                // 已经连接
                ft.replace(R.id.container_fl, BluetoothMusicFragment.newInstance());
            } else {
                // 未连接
                ft.replace(R.id.container_fl, NeedAddDeviceFragment.newInstance());
            }
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}