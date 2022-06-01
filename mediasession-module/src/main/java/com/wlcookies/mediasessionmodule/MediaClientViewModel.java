package com.wlcookies.mediasessionmodule;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.media2.common.MediaMetadata;

/**
 * 媒体客户端ViewModel
 */
public class MediaClientViewModel extends ViewModel {

    /**
     * 播放状态
     */
    private final MutableLiveData<Integer> _playState = new MutableLiveData<>();
    public LiveData<Integer> playState = _playState;

    public void setPlayState(int state) {
        _playState.setValue(state);
    }

    /**
     * 连接状态
     */
    private final MutableLiveData<Boolean> _connectState = new MutableLiveData<>();
    public LiveData<Boolean> connectState = _connectState;

    public void setConnectState(boolean isConnect) {
        _connectState.setValue(isConnect);
    }

    /**
     * 当前媒体数据
     */
    private final MutableLiveData<MediaMetadata> _currentMediaItem = new MutableLiveData<>();
    public LiveData<MediaMetadata> currentMediaItem = _currentMediaItem;

    public void setCurrentMediaItem(@NonNull MediaMetadata mediaMetadata) {
        _currentMediaItem.setValue(mediaMetadata);
    }

    /**
     * 更新进度
     */
    private final MutableLiveData<Integer> _currentPosition = new MutableLiveData<>();
    public LiveData<Integer> currentPosition = _currentPosition;

    public void setCurrentPosition(int position) {
        _currentPosition.setValue(position);
    }


    /**
     * 获取当前媒体ICON
     *
     * @param mediaMetadata 媒体数据
     * @return Bitmap
     */
    public @Nullable
    Bitmap getMediaIcon(@NonNull MediaMetadata mediaMetadata) {
        return mediaMetadata.getBitmap(MediaMetadata.METADATA_KEY_DISPLAY_ICON);
    }

    /**
     * 获取专辑名称
     *
     * @param mediaMetadata 媒体数据
     * @return 专辑名称
     */
    public @NonNull
    String getMediaAlbum(@NonNull MediaMetadata mediaMetadata) {
        String album = mediaMetadata.getString(MediaMetadata.METADATA_KEY_ALBUM);
        return album != null ? album : "未知";
    }

    /**
     * 获取标题
     *
     * @param mediaMetadata 媒体数据
     * @return 标题名称
     */
    public @NonNull
    String getMediaDisplayTitle(@NonNull MediaMetadata mediaMetadata) {
        String prefsTitle = mediaMetadata.getString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE);
        String optionsTitle = mediaMetadata.getString(MediaMetadata.METADATA_KEY_TITLE);
        return prefsTitle != null ? prefsTitle : (optionsTitle != null ? optionsTitle : "未知");
    }

    /**
     * 获取副标题
     *
     * @param mediaMetadata 媒体数据
     * @return 副标题名称
     */
    public @NonNull
    String getMediaDisplaySubTitle(@NonNull MediaMetadata mediaMetadata) {
        String displaySubTitle = mediaMetadata.getString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE);
        return displaySubTitle != null ? displaySubTitle : "未知";
    }

    /**
     * 获取总时长
     *
     * @param mediaMetadata 媒体数据
     * @return 总时长
     */
    public long getMediaDuration(@NonNull MediaMetadata mediaMetadata) {
        return mediaMetadata.getLong(MediaMetadata.METADATA_KEY_DURATION);
    }
}
