package com.wlcookies.fundemo.ui.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.media2.common.MediaMetadata;
import androidx.media2.session.MediaBrowser;

import com.wlcookies.commonmodule.media.client.CurrentPlayingComponent;
import com.wlcookies.commonmodule.media.client.MediaClient;
import com.wlcookies.commonmodule.media.client.MediaClientViewModel;
import com.wlcookies.commonmodule.media.client.MediaSourceChangeClient;
import com.wlcookies.commonmodule.utils.LogUtils;
import com.wlcookies.fundemo.BaseActivity;
import com.wlcookies.fundemo.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

/**
 * com.netease.cloudmusic              com.netease.cloudmusic.module.ucar.UCarService
 * com.example.android.mediasession    com.example.android.mediasession.service.MusicService
 */
public class MediaClientChangeActivity extends BaseActivity {

    public static Intent newInstance(Context context) {
        return new Intent(context, MediaClientChangeActivity.class);
    }

    /**
     * com.bmit.miguservice.mediasession.MiGuSessionService
     * com.bmit.yuntingservice.mediasession.YunTingSessionService
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meida_client_change);


        MediaClientViewModel mediaClientViewModel = new ViewModelProvider(this).get(MediaClientViewModel.class);

//        MediaClient mediaClient = new MediaClient(this, "com.netease.cloudmusic.module.ucar.UCarService", null);
//        mediaClient.logMediaSessionSupportList();
//        mediaClient.setMediaClientViewModel(mediaClientViewModel);

        ArrayList<String> strings = new ArrayList<>();
//        strings.add("com.example.android.mediasession.service.MusicService");
//        strings.add("com.netease.cloudmusic.module.ucar.UCarService");
//        strings.add("com.bmit.miguservice.mediasession.MiGuSessionService");
        strings.add("com.bmit.yuntingservice.mediasession.YunTingSessionService");
        MediaSourceChangeClient mediaSourceChangeClient = new MediaSourceChangeClient(this, strings, mediaClientViewModel);



        MediaClient mediaClient = new MediaClient(this, "", mediaClientViewModel, null);
        LogUtils.d("000000000000000000000000000000000000000000000000000000");
        mediaClient.logMediaSessionSupportList();

        mediaClientViewModel.initMediaBrowserResultCollection.observe(this, new Observer<Map<String, Boolean>>() {
            @Override
            public void onChanged(Map<String, Boolean> stringBooleanMap) {
                LogUtils.d("初始化：" + stringBooleanMap);
            }
        });

        LogUtils.d("当前的播放源 ： " + mediaClientViewModel.currentPlayingComponentName.getValue());
        mediaClientViewModel.currentPlayingComponentName.observe(this, currentPlayingComponent -> {
            LogUtils.d("当前的播放源 " + currentPlayingComponent);
        });

        mediaClientViewModel.connectStateCollection.observe(this, connectStateMap -> {
            LogUtils.d("连接状态： " + connectStateMap);
            LogUtils.d("连接状态==Size： " + connectStateMap.size());
        });

        mediaClientViewModel.currentPlayingComponentName.observe(this, pkgName -> {
            LogUtils.d("正在播放： " + pkgName);
        });

        mediaClientViewModel.playStateCollection.observe(this, new Observer<Map<String, Integer>>() {
            @Override
            public void onChanged(Map<String, Integer> stringIntegerMap) {
                LogUtils.d("播放状态" + stringIntegerMap);
            }
        });

        mediaClientViewModel.currentMediaItemCollection.observe(this, new Observer<Map<String, MediaMetadata>>() {
            @Override
            public void onChanged(Map<String, MediaMetadata> stringMediaMetadataMap) {
                LogUtils.d("当前播放媒体 " + stringMediaMetadataMap);
            }
        });

        mediaClientViewModel.currentPositionCollection.observe(this, new Observer<Map<String, Integer>>() {
            @Override
            public void onChanged(Map<String, Integer> stringIntegerMap) {
//                LogUtils.d("当前播放进度 " + stringIntegerMap);
            }
        });

        Button nextBt = findViewById(R.id.next_bt);
        Button puaseBt = findViewById(R.id.puase_bt);
        Button playBt = findViewById(R.id.play_bt);
        nextBt.setOnClickListener(v -> {
            mediaSourceChangeClient.skipToNext("com.bmit.miguservice.mediasession.MiGuSessionService");
//            mediaSourceChangeClient.skipToNext("com.example.android.mediasession.service.MusicService");
//            mediaSourceChangeClient.skipToNext("com.bmit.yuntingservice.mediasession.YunTingSessionService");
        });
        puaseBt.setOnClickListener(v -> {
            mediaSourceChangeClient.pause("com.bmit.miguservice.mediasession.MiGuSessionService");
//            mediaSourceChangeClient.pause("com.bmit.yuntingservice.mediasession.YunTingSessionService");
        });
        playBt.setOnClickListener(v -> {
            mediaSourceChangeClient.play("com.bmit.miguservice.mediasession.MiGuSessionService");
//            mediaSourceChangeClient.play("com.bmit.yuntingservice.mediasession.YunTingSessionService");
        });
    }
}