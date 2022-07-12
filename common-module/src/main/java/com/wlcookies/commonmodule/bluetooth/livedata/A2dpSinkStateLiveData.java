package com.wlcookies.commonmodule.bluetooth.livedata;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.IntDef;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class A2dpSinkStateLiveData extends LiveData<Integer> {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            BluetoothA2dpSinkState.STATE_DISCONNECTED,
            BluetoothA2dpSinkState.STATE_CONNECTING,
            BluetoothA2dpSinkState.STATE_CONNECTED,
            BluetoothA2dpSinkState.STATE_DISCONNECTING,
    })
    public @interface BluetoothA2dpSinkState {
        int STATE_DISCONNECTED = 0;
        int STATE_CONNECTING = 1;
        int STATE_CONNECTED = 2;
        int STATE_DISCONNECTING = 3;
    }

    private final Context mContext;
    private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final IntentFilter mIntentFilter = new IntentFilter();

    @MainThread
    public A2dpSinkStateLiveData(@NonNull Context context) {
        mContext = context;
        mIntentFilter.addAction("android.bluetooth.a2dp-sink.profile.action.CONNECTION_STATE_CHANGED");
    }

    private final BroadcastReceiver mA2dpSinkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateState();
        }
    };

    @Override
    protected void onActive() {
        if (mBluetoothAdapter != null) {
            updateState();
            mContext.registerReceiver(mA2dpSinkStateReceiver, mIntentFilter);
        }
    }

    @Override
    protected void onInactive() {
        if (mBluetoothAdapter != null) {
            mContext.unregisterReceiver(mA2dpSinkStateReceiver);
        }
    }

    @SuppressLint("MissingPermission")
    private void updateState() {
        @BluetoothA2dpSinkState int state;
        if (mBluetoothAdapter != null) {
            int profileConnectionState = mBluetoothAdapter.getProfileConnectionState(11);
            switch (profileConnectionState) {
                case BluetoothProfile.STATE_CONNECTING:
                    state = BluetoothA2dpSinkState.STATE_CONNECTING;
                    break;
                case BluetoothProfile.STATE_CONNECTED:
                    state = BluetoothA2dpSinkState.STATE_CONNECTED;
                    break;
                case BluetoothProfile.STATE_DISCONNECTING:
                    state = BluetoothA2dpSinkState.STATE_DISCONNECTING;
                    break;
                default:
                    state = BluetoothA2dpSinkState.STATE_DISCONNECTED;
            }
        } else {
            state = BluetoothA2dpSinkState.STATE_DISCONNECTED;
        }
        if (getValue() == null || state != getValue()) {
            setValue(state);
        }
    }
}
