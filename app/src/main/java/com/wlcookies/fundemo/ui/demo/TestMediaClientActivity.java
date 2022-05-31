package com.wlcookies.fundemo.ui.demo;


import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media2.common.MediaItem;
import androidx.media2.common.MediaMetadata;
import androidx.media2.common.SessionPlayer;
import androidx.media2.session.MediaBrowser;
import androidx.media2.session.MediaController;

import com.google.android.material.imageview.ShapeableImageView;
import com.wlcookies.commonmodule.utils.DateUtils;
import com.wlcookies.fundemo.R;
import com.wlcookies.mediasessionmodule.MediaClient;
import com.wlcookies.mediasessionmodule.MediaClientViewModel;

public class TestMediaClientActivity extends AppCompatActivity {

    private MediaClient mediaClient;
    private SeekBar seekBar;

    public static Intent newInstance(Context context) {
        return new Intent(context, TestMediaClientActivity.class);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_media_client);

        mediaClient = new MediaClient(this, "com.netease.cloudmusic", null); // 网易云音乐
//        mediaClient = new MediaClient(this, "com.android.bluetooth", null); // 系统蓝牙
//        mediaClient = new MediaClient(this, "com.example.android.mediasession", null); // 系统蓝牙
        MediaClientViewModel mediaClientViewModel = mediaClient.getDataViewModel(this);

        Button skipToPrevious = findViewById(R.id.skipToPrevious);
        Button play = findViewById(R.id.play);
        Button puase = findViewById(R.id.puase);
        Button skipToNext = findViewById(R.id.skipToNext);
        TextView playState = findViewById(R.id.play_state);
        TextView album = findViewById(R.id.album);
        TextView title = findViewById(R.id.title);
        TextView subtitle = findViewById(R.id.subtitle);
        TextView total = findViewById(R.id.total);
        TextView current = findViewById(R.id.current);
        seekBar = findViewById(R.id.seekBar);

        ShapeableImageView icon = findViewById(R.id.icon);
        mediaClient.setSeekBar(seekBar);

        // 上一首
        skipToPrevious.setOnClickListener(v -> {
            mediaClient.skipToPrevious();
        });

        // 下一首
        skipToNext.setOnClickListener(v -> {
            mediaClient.skipToNext();
        });

        // 播放
        play.setOnClickListener(v -> {
            mediaClient.play();
        });

        // 暂停
        puase.setOnClickListener(v -> {
            mediaClient.pause();
        });

        // 连接状态
        mediaClientViewModel.connectState.observe(this, isConnected -> {
            if (isConnected) {
                // 连接成功
                MediaBrowser mediaController = mediaClient.getMediaController();
                long currentPosition = mediaController.getCurrentPosition() == SessionPlayer.UNKNOWN_TIME ? 0 : mediaController.getCurrentPosition();
                MediaItem currentMediaItem = mediaController.getCurrentMediaItem();
                if (currentMediaItem != null) {
                    MediaMetadata playlistMetadata = currentMediaItem.getMetadata();
                    if (playlistMetadata != null) {
                        int mediaDuration = (int) mediaClientViewModel.getMediaDuration(playlistMetadata);
                        total.setText(DateUtils.hhmm(mediaDuration));
                        seekBar.setMax(mediaDuration);
                        seekBar.setProgress((int) currentPosition);
                    }
                }
            }
        });

        // 播放状态
        mediaClientViewModel.playState.observe(this, state -> {
            playState.setText("" + state);
        });

        mediaClientViewModel.currentMediaItem.observe(this, mediaMetadata -> {
            Bitmap mediaIcon = mediaClientViewModel.getMediaIcon(mediaMetadata);
            if (mediaIcon != null) {
                icon.setImageBitmap(mediaIcon);
            }
            album.setText(mediaClientViewModel.getMediaAlbum(mediaMetadata));
            title.setText(mediaClientViewModel.getMediaDisplayTitle(mediaMetadata));
            subtitle.setText(mediaClientViewModel.getMediaDisplaySubTitle(mediaMetadata));
            subtitle.setText(mediaClientViewModel.getMediaDisplaySubTitle(mediaMetadata));
            total.setText(DateUtils.hhmm((int) mediaClientViewModel.getMediaDuration(mediaMetadata)));
            // 设置最大值
            seekBar.setMax((int) mediaClientViewModel.getMediaDuration(mediaMetadata));
        });

        mediaClientViewModel.currentPosition.observe(this, progress -> {
            if (!MediaClient.isSeeking) {
                seekBar.setProgress(progress);
            }
            current.setText(DateUtils.hhmm(progress));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaClient.close();
    }
}