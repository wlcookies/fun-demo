package com.wlcookies.fundemo.ui.demo;

import static com.wlcookies.mediasessionmodule.MediaClient.log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.media2.common.MediaMetadata;
import androidx.media2.common.SessionPlayer;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.google.android.material.imageview.ShapeableImageView;
import com.wlcookies.commonmodule.utils.DateUtils;
import com.wlcookies.fundemo.R;
import com.wlcookies.mediasessionmodule.MediaClient;
import com.wlcookies.mediasessionmodule.MediaClientViewModel;

import java.util.List;

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
        MediaClientViewModel mediaClientViewModel = new ViewModelProvider(this).get(MediaClientViewModel.class);
        mediaClient = new MediaClient(this, "com.netease.cloudmusic", null);
        mediaClient.setMediaClientViewModel(mediaClientViewModel);

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

        // 调整进度位置
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                MediaClient.isSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MediaClient.isSeeking = false;
                mediaClient.seekTo(seekBar.getProgress());
            }
        });

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
            log("连接状态 " + isConnected);
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
            seekBar.setProgress(progress);
            current.setText(DateUtils.hhmm(progress));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaClient.close();
    }
}