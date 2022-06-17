package com.wlcookies.fundemo.ui.yunting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.kaolafm.sdk.client.ErrorInfo;
import com.kaolafm.sdk.client.KLClientAPI;
import com.kaolafm.sdk.client.Music;
import com.kaolafm.sdk.client.PlayState;
import com.kaolafm.sdk.client.PlayStateListener;
import com.wlcookies.commonmodule.utils.LogUtils;
import com.wlcookies.commonmodule.utils.SafetyUtils;
import com.wlcookies.fundemo.R;

/**
 * 云听SDK测试
 */
public class YunTingActivity extends AppCompatActivity {

    public static Intent newInstance(Context context) {
        return new Intent(context, YunTingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yun_ting);

        ImageView iconIv = findViewById(R.id.icon_iv);

        Music currentMusicInfo = KLClientAPI.getInstance().getCurrentMusicInfo();
        LogUtils.d("当前播放： " + currentMusicInfo);

        String playStateName = KLClientAPI.getInstance().getPlayState().name();
        LogUtils.d("当前播放状态： " + playStateName);

        KLClientAPI.getInstance().setPlayListener(new PlayStateListener() {

            @Override
            public void onStartPrepare(Music music) {
                LogUtils.d("准备开始播放:  " + SafetyUtils.getString(music.albumName));
            }

            @Override
            public void onPlaying(Music music) {
                if (music != null) {
                    LogUtils.d("onPlaying：------------------------------");
                    LogUtils.d(SafetyUtils.getString(music.albumName));
                    LogUtils.d(SafetyUtils.getString(music.audioName));
                    LogUtils.d(SafetyUtils.getString(music.authorName));
                    LogUtils.d(SafetyUtils.getString(music.categoryName));
                    LogUtils.d("onPlaying：------------------------------");
                }
            }

            @Override
            public void onProgress(Music music, long l) {
                LogUtils.d("播放进度：: " + l);
            }

            @Override
            public void onPause(Music music) {
                if (music != null) {
                    LogUtils.d("onPause：------------------------------");
                    LogUtils.d(SafetyUtils.getString(music.albumName));
                    LogUtils.d(SafetyUtils.getString(music.audioName));
                    LogUtils.d(SafetyUtils.getString(music.authorName));
                    LogUtils.d(SafetyUtils.getString(music.categoryName));
                    LogUtils.d("onPause：------------------------------");
                }
            }

            @Override
            public void onPlayMusic(Music music) {
                try {
                    if (music != null) {
                        LogUtils.d("onPlayMusic：------------------------------");
                        LogUtils.d(SafetyUtils.getString(music.albumName));
                        LogUtils.d(SafetyUtils.getString(music.audioName));
                        LogUtils.d(SafetyUtils.getString(music.authorName));
                        LogUtils.d(SafetyUtils.getString(music.categoryName));
                        LogUtils.d(SafetyUtils.getString(music.localPicUri));
                        LogUtils.d(SafetyUtils.getString(music.localPlayUrl));
                        LogUtils.d(SafetyUtils.getString(music.picUrl));

                        runOnUiThread(() -> Glide.with(YunTingActivity.this)
                                .load(SafetyUtils.getString(music.picUrl))
                                .circleCrop()
                                .into(iconIv));
                        //
                        LogUtils.d("onPlayMusic：------------------------------ " + Thread.currentThread().getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                LogUtils.d("onError: " + errorInfo.errCode);
                LogUtils.d("onError: " + errorInfo.info);
            }

            @Override
            public void onPlayStateChange(PlayState playState, int i, Music music) {
                LogUtils.d("onPlayStateChange: " + i);
            }
        });

    }
}