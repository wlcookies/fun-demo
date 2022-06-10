package com.wlcookies.fundemo.ui.yunting;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.kaolafm.sdk.client.ErrorInfo;
import com.kaolafm.sdk.client.IServiceConnection;
import com.kaolafm.sdk.client.KLClientAPI;
import com.kaolafm.sdk.client.Music;
import com.kaolafm.sdk.client.PlayState;
import com.kaolafm.sdk.client.PlayStateListener;
import com.wlcookies.commonmodule.utils.SafetyUtils;
import com.wlcookies.fundemo.R;

/**
 * 云听SDK测试
 */
public class YunTingActivity extends AppCompatActivity {

    private static final String TAG = "YunTingActivity";

    public static Intent newInstance(Context context) {
        return new Intent(context, YunTingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yun_ting);

        ImageView iconIv = findViewById(R.id.icon_iv);

        KLClientAPI.getInstance().setPlayListener(new PlayStateListener() {
            @Override
            public void onStartPrepare(Music music) {
                Log.d(TAG, "准备开始播放: " + SafetyUtils.getString(music.albumName));
            }

            @Override
            public void onPlaying(Music music) {
                Log.d(TAG, "onPlaying：------------------------------");
                Log.d(TAG, SafetyUtils.getString(music.albumName));
                Log.d(TAG, SafetyUtils.getString(music.audioName));
                Log.d(TAG, SafetyUtils.getString(music.authorName));
                Log.d(TAG, SafetyUtils.getString(music.categoryName));
                Log.d(TAG, "onPlaying：------------------------------");
            }

            @Override
            public void onProgress(Music music, long l) {
                Log.d(TAG, "播放进度：: " + l);
            }

            @Override
            public void onPause(Music music) {
                Log.d(TAG, "onPause：------------------------------");
                Log.d(TAG, SafetyUtils.getString(music.albumName));
                Log.d(TAG, SafetyUtils.getString(music.audioName));
                Log.d(TAG, SafetyUtils.getString(music.authorName));
                Log.d(TAG, SafetyUtils.getString(music.categoryName));
                Log.d(TAG, "onPause：------------------------------");
            }

            @Override
            public void onPlayMusic(Music music) {
                try {

                    if (music != null) {
                        Log.d(TAG, "onPlayMusic：------------------------------");
                        Log.d(TAG, SafetyUtils.getString(music.albumName));
                        Log.d(TAG, SafetyUtils.getString(music.audioName));
                        Log.d(TAG, SafetyUtils.getString(music.authorName));
                        Log.d(TAG, SafetyUtils.getString(music.categoryName));
                        Log.d(TAG, SafetyUtils.getString(music.localPicUri));
                        Log.d(TAG, SafetyUtils.getString(music.localPlayUrl));
                        Log.d(TAG, SafetyUtils.getString(music.picUrl));

                        runOnUiThread(() -> Glide.with(YunTingActivity.this)
                                .load(SafetyUtils.getString(music.picUrl))
                                .circleCrop()
                                .into(iconIv));
                        //
                        Log.d(TAG, "onPlayMusic：------------------------------ " + Thread.currentThread().getName());
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
                Log.d(TAG, "onError: " + errorInfo.errCode);
            }

            @Override
            public void onPlayStateChange(PlayState playState, int i, Music music) {
                Log.d(TAG, "onPlayStateChange: " + i);
            }
        });

    }
}