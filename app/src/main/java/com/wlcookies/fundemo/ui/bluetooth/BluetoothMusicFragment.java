package com.wlcookies.fundemo.ui.bluetooth;

import android.bluetooth.BluetoothA2dp;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.media2.common.MediaItem;
import androidx.media2.common.MediaMetadata;
import androidx.media2.common.SessionPlayer;
import androidx.media2.session.MediaBrowser;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.wlcookies.commonmodule.utils.DateUtils;
import com.wlcookies.commonmodule.utils.SafetyUtils;
import com.wlcookies.fundemo.R;
import com.wlcookies.mediasessionmodule.MediaClient;
import com.wlcookies.mediasessionmodule.MediaClientViewModel;

/**
 * 蓝牙音乐界面
 */
public class BluetoothMusicFragment extends Fragment {

    private static final String TAG = "BluetoothMusicFragment";

    private Animation rotateAnimation;
    private UnableDragSeekBar mSeekBar;
    private TextView mCurrentTimeTv;
    private TextView mTotalTimeTv;
    private ImageView mPlayIv;
    private ImageView mPrevIv;
    private ImageView mNextIv;
    private MediaClient mMediaClient;

    private boolean isPlaying = false; // 是否播放
    private int mediaDuration = 0; // 当前媒体播放时长

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

        // 旋转动画
        rotateAnimation = AnimationUtils.loadAnimation(requireActivity(), R.anim.anim_bluetooth_music_icon_rotate);
        mSeekBar = view.findViewById(R.id.progress_sb);
        mCurrentTimeTv = view.findViewById(R.id.current_time_tv);
        mTotalTimeTv = view.findViewById(R.id.total_time_tv);
        mPlayIv = view.findViewById(R.id.play_iv);
        mPrevIv = view.findViewById(R.id.prev_iv);
        mNextIv = view.findViewById(R.id.next_iv);

        mSeekBar.setTouch(false); // 设置不可拖拽

        mMediaClient = new MediaClient(requireActivity(), "com.android.bluetooth", null);
        MediaClientViewModel dataViewModel = mMediaClient.getDataViewModel(this);
        mMediaClient.setSeekBar(mSeekBar);

        // 播放或暂停
        mPlayIv.setOnClickListener(v -> {

            jumpToAddDevice();

            if (mMediaClient != null) {
                if (isPlaying) {
                    mMediaClient.pause();
                } else {
                    mMediaClient.play();
                }
            }
        });

        // 上一曲
        mPrevIv.setOnClickListener(v -> {
            if (mMediaClient != null) {
                mMediaClient.skipToPrevious();
            }
        });

        // 下一曲
        mNextIv.setOnClickListener(v -> {
            if (mMediaClient != null) {
                mMediaClient.skipToNext();
            }
        });
        // 组件初始化结果
        dataViewModel.initMediaBrowserResult.observe(getViewLifecycleOwner(), result -> {
            // 没有找到对应MediaSession服务
            Log.d(TAG, "组件初始化结果: " + result);
        });
        // 播放状态
        dataViewModel.playState.observe(getViewLifecycleOwner(), playState -> {
            Log.d(TAG, "播放状态 === " + playState);
            // 是否在播放
            isPlaying = playState == SessionPlayer.PLAYER_STATE_PLAYING;
            mPlayIv.setImageDrawable(isPlaying ?
                    ResourcesCompat.getDrawable(getResources(), R.drawable.ic_bluetooth_music_next, null) : ResourcesCompat.getDrawable(getResources(), R.drawable.ic_bluetooth_music_play, null));
        });

        // 连接状态
        dataViewModel.connectState.observe(getViewLifecycleOwner(), isConnected -> {
            Log.d(TAG, "连接状态 === " + isConnected);
            if (isConnected) {
                // 初始化媒体
                if (mMediaClient != null) {
                    MediaBrowser mediaBrowser = mMediaClient.getMediaController();
                    if (mediaBrowser != null && mSeekBar != null && mCurrentTimeTv != null && mTotalTimeTv != null) {
                        int currentPosition = mediaBrowser.getCurrentPosition() == SessionPlayer.UNKNOWN_TIME ? 0 : (int) mediaBrowser.getCurrentPosition();
                        mediaDuration = mediaBrowser.getDuration() == SessionPlayer.UNKNOWN_TIME ? 0 : (int) mediaBrowser.getDuration();
                        // 设置进度
                        mSeekBar.setMax(mediaDuration);
                        mSeekBar.setProgress(currentPosition);
                        // 设置时间显示
                        if (currentPosition >= mediaDuration) {
                            currentPosition = mediaDuration;
                        }
                        // 当前时长
                        mCurrentTimeTv.setText(DateUtils.hhmm(currentPosition));
                        // 总时长
                        mTotalTimeTv.setText(DateUtils.hhmm(mediaDuration));
                        // 初始化媒体数据
                        MediaItem currentMediaItem = mediaBrowser.getCurrentMediaItem();
                        if (currentMediaItem != null) {
                            MediaMetadata metadata = currentMediaItem.getMetadata();
                            updateMediaMetadata(metadata);
                        }
                    }
                }
            } else {
                jumpToAddDevice();
            }
        });

        // 当前播放媒体
        dataViewModel.currentMediaItem.observe(getViewLifecycleOwner(), mediaMetadata -> {
            Log.d(TAG, "当前播放媒体 " + dataViewModel.getMediaDisplayTitle(mediaMetadata));

            mediaDuration = (int) dataViewModel.getMediaDuration(mediaMetadata);
            mSeekBar.setMax(mediaDuration);

            updateMediaMetadata(mediaMetadata);

        });

        // 当前位置
        dataViewModel.currentPosition.observe(getViewLifecycleOwner(), progress -> {
            Log.d(TAG, "当前播放进度 " + progress);
            if (!MediaClient.isSeeking) {
                mSeekBar.setProgress(progress);
            }
            if (progress >= mediaDuration) {
                progress = mediaDuration;
            }
            // 当前时长
            mCurrentTimeTv.setText(DateUtils.hhmm(progress));
            // 总时长
            mTotalTimeTv.setText(DateUtils.hhmm(mediaDuration));
        });
    }

    private void jumpToAddDevice() {
        try {
            BluetoothMusicActivity bluetoothMusicActivity = (BluetoothMusicActivity) getActivity();
            if (bluetoothMusicActivity != null) {
                bluetoothMusicActivity.a2dpStateChange(BluetoothA2dp.STATE_DISCONNECTED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新媒体数据，不包含进度
     *
     * @param metadata 媒体数据
     */
    private void updateMediaMetadata(MediaMetadata metadata) {
        if (metadata != null) {
            String keyTitle = SafetyUtils.getString(metadata.getString(MediaMetadata.METADATA_KEY_TITLE));
            String keyArtist = SafetyUtils.getString(metadata.getString(MediaMetadata.METADATA_KEY_ARTIST));
            String keyAlbum = SafetyUtils.getString(metadata.getString(MediaMetadata.METADATA_KEY_ALBUM));
            String keyGenre = SafetyUtils.getString(metadata.getString(MediaMetadata.METADATA_KEY_GENRE));

            Log.d(TAG, "METADATA_KEY_TITLE: " + keyTitle);
            Log.d(TAG, "METADATA_KEY_ARTIST: " + keyArtist);
            Log.d(TAG, "METADATA_KEY_ALBUM: " + keyAlbum);
            Log.d(TAG, "METADATA_KEY_GENRE: " + keyGenre);

            Log.d(TAG, "METADATA_KEY_DURATION: " + metadata.getLong(MediaMetadata.METADATA_KEY_DURATION));
            Log.d(TAG, "METADATA_KEY_TRACK_NUMBER: " + metadata.getLong(MediaMetadata.METADATA_KEY_TRACK_NUMBER));
            Log.d(TAG, "METADATA_KEY_NUM_TRACKS: " + metadata.getLong(MediaMetadata.METADATA_KEY_NUM_TRACKS));

            Log.d(TAG, "start --- 支持的所有====================================KEY");
            for (String s : metadata.keySet()) {
                Log.d(TAG, s);
            }
            Log.d(TAG, "end --- 支持的所有====================================KEY");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rotateAnimation != null) {
            rotateAnimation.cancel();
        }
        if (mMediaClient != null) {
            mMediaClient.close();
        }
    }
}