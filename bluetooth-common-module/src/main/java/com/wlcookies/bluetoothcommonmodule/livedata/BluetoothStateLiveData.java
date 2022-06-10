package com.wlcookies.bluetoothcommonmodule.livedata;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.IntDef;
import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;

/**
 * Provides the device Bluetooth availability.
 * Updates client with {@link BluetoothState}.
 */
public class BluetoothStateLiveData extends LiveData<Integer> {

    @IntDef({
            BluetoothState.UNKNOWN,
            BluetoothState.DISABLED,
            BluetoothState.ENABLED,
    })
    public @interface BluetoothState {
        /**
         * Bluetooth is not supported on the current device
         */
        int UNKNOWN = 0;
        /**
         * Bluetooth is disabled
         */
        int DISABLED = 1;
        /**
         * Bluetooth is enabled
         */
        int ENABLED = 2;
    }

    private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final Context mContext;
    private final IntentFilter mIntentFilter = new IntentFilter();

    private final BroadcastReceiver mBluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateState();
        }
    };

    @MainThread
    public BluetoothStateLiveData(Context context) {
        mContext = context;
        mIntentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
    }

    @Override
    protected void onActive() {
        if (mBluetoothAdapter != null) {
            updateState();
            mContext.registerReceiver(mBluetoothStateReceiver, mIntentFilter);
        }
    }

    @Override
    protected void onInactive() {
        if (mBluetoothAdapter != null) {
            mContext.unregisterReceiver(mBluetoothStateReceiver);
        }
    }

    private void updateState() {
        @BluetoothState int state = BluetoothState.UNKNOWN;
        if (mBluetoothAdapter != null) {
            state = mBluetoothAdapter.isEnabled() ? BluetoothState.ENABLED
                    : BluetoothState.DISABLED;
        }
        if (getValue() == null || state != getValue()) {
            setValue(state);
        }
    }
}
