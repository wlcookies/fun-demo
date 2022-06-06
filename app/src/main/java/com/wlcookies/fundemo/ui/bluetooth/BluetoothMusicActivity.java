package com.wlcookies.fundemo.ui.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;

import com.wlcookies.fundemo.R;
import com.wlcookies.mediasessionmodule.MediaClient;

/**
 * 蓝牙音乐
 */
public class BluetoothMusicActivity extends AppCompatActivity {

    public static Intent newInstance(Context context) {
        return new Intent(context, BluetoothMusicActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_music);

//        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
//        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        // 主动判断
//        bluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP);
        // 注册广播判断

        MediaClient mediaClient = new MediaClient(this, "", null);
        mediaClient.logMediaSessionSupportList();
    }

//    public void registerBtReceiver(Context context) {
//        IntentFilter intentFilter = new IntentFilter();
//        //A2DP连接状态改变
//        BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED
//        intentFilter.addAction(BluetoothA2dpSink.ACTION_CONNECTION_STATE_CHANGED);
//        //A2DP播放状态改变
//        intentFilter.addAction(BluetoothA2dpSink.ACTION_PLAYING_STATE_CHANGED);
//        //监听蓝牙音乐暂停、播放等
//        intentFilter.addAction(BluetoothAvrcpController.ACTION_TRACK_EVENT);
//        //连接状态
//        intentFilter.addAction(BluetoothAvrcpController.ACTION_CONNECTION_STATE_CHANGED);
//        //浏览
//        intentFilter.addAction(BluetoothAvrcpController.ACTION_BROWSE_CONNECTION_STATE_CHANGED);
//        // 正在浏览的事件
//        intentFilter.addAction(BluetoothAvrcpController.ACTION_BROWSING_EVENT);
//        //当前 媒体 项目 改变
//        intentFilter.addAction(BluetoothAvrcpController.ACTION_CURRENT_MEDIA_ITEM_CHANGED);
//        intentFilter.addAction(BluetoothAvrcpController.ACTION_PLAYER_SETTING);
//        //没有媒体信息
//        intentFilter.addAction(BluetoothAvrcpController.ACTION_PLAY_FAILURE);
//        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
//        intentFilter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
//
//        context.registerReceiver(mBtReceiver, intentFilter);
//    }
}