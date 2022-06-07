package com.wlcookies.fundemo.ui.bluetooth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.wlcookies.fundemo.R;

/**
 * 蓝牙音乐界面
 */
public class BluetoothMusicFragment extends Fragment {

    private static final String TAG = "BluetoothMusicFragment";

    private Animation rotateAnimation;

    public static BluetoothMusicFragment newInstance() {
        return new BluetoothMusicFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bluetooth_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView testIv = view.findViewById(R.id.test_iv);
        rotateAnimation = AnimationUtils.loadAnimation(requireActivity(), R.anim.anim_bluetooth_music_icon_rotate);
        testIv.startAnimation(rotateAnimation);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rotateAnimation != null) {
            rotateAnimation.cancel();
        }
    }
}