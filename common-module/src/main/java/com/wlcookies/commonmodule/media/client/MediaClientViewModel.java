package com.wlcookies.commonmodule.media.client;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.media2.common.MediaMetadata;

import com.wlcookies.commonmodule.utils.LogUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 媒体客户端ViewModel
 */
public class MediaClientViewModel extends ViewModel {

    /**
     * current playing media session service package name
     */
    private final MutableLiveData<String> _currentPlayingComponentName = new MutableLiveData<>();
    public LiveData<String> currentPlayingComponentName = _currentPlayingComponentName;

    public void setCurrentPlayingComponentName(String pkgName) {
        _currentPlayingComponentName.postValue(pkgName);
    }

    /**
     * 初始化组件的结果
     */
    private final MutableLiveData<Boolean> _initMediaBrowserResult = new MutableLiveData<>();
    public LiveData<Boolean> initMediaBrowserResult = _initMediaBrowserResult;

    private final MutableLiveData<Map<String, Boolean>> _initMediaBrowserResultCollection = new MutableLiveData<>();
    public LiveData<Map<String, Boolean>> initMediaBrowserResultCollection = _initMediaBrowserResultCollection;

    public void setInitMediaBrowserResult(String serviceName, boolean result) {
        _initMediaBrowserResult.setValue(result);
        Map<String, Boolean> value = _initMediaBrowserResultCollection.getValue();
        if (value == null) {
            value = new HashMap<>();
        }
        if (serviceName != null && !"".equals(serviceName)) {
            value.put(serviceName, result);
            _initMediaBrowserResultCollection.setValue(value);
        }
    }

    /**
     * 播放状态
     */
    private final MutableLiveData<Integer> _playState = new MutableLiveData<>();
    public LiveData<Integer> playState = _playState;

    private final MutableLiveData<Map<String, Integer>> _playStateCollection = new MutableLiveData<>();
    public LiveData<Map<String, Integer>> playStateCollection = _playStateCollection;

    public void setPlayState(String serviceName, int state) {
        _playState.setValue(state);
        Map<String, Integer> value = _playStateCollection.getValue();
        if (value == null) {
            value = new HashMap<>();
        }
        if (serviceName != null && !"".equals(serviceName)) {
            value.put(serviceName, state);
            _playStateCollection.setValue(value);
        }
    }

    /**
     * 连接状态
     */
    private final MutableLiveData<Boolean> _connectState = new MutableLiveData<>();
    public LiveData<Boolean> connectState = _connectState;

    private final MutableLiveData<Map<String, Boolean>> _connectStateCollection = new MutableLiveData<>();
    public LiveData<Map<String, Boolean>> connectStateCollection = _connectStateCollection;

    public void setConnectState(String serviceName, boolean isConnect) {
        _connectState.setValue(isConnect);
        Map<String, Boolean> value = _connectStateCollection.getValue();
        if (value == null) {
            value = new HashMap<>();
        }
        if (serviceName != null && !"".equals(serviceName)) {
            value.put(serviceName, isConnect);
            _connectStateCollection.setValue(value);
        }
    }

    /**
     * 当前媒体数据
     */
    private final MutableLiveData<MediaMetadata> _currentMediaItem = new MutableLiveData<>();
    public LiveData<MediaMetadata> currentMediaItem = _currentMediaItem;

    private final MutableLiveData<Map<String, MediaMetadata>> _currentMediaItemCollection = new MutableLiveData<>();
    public LiveData<Map<String, MediaMetadata>> currentMediaItemCollection = _currentMediaItemCollection;

    public void setCurrentMediaItem(String serviceName, MediaMetadata mediaMetadata) {
        _currentMediaItem.postValue(mediaMetadata);
        Map<String, MediaMetadata> value = _currentMediaItemCollection.getValue();
        if (value == null) {
            value = new HashMap<>();
        }
        if (serviceName != null && !"".equals(serviceName)) {
            value.put(serviceName, mediaMetadata);
            _currentMediaItemCollection.postValue(value);
        }
    }

    /**
     * 更新进度
     */
    private final MutableLiveData<Integer> _currentPosition = new MutableLiveData<>();
    public LiveData<Integer> currentPosition = _currentPosition;

    private final MutableLiveData<Map<String, Integer>> _currentPositionCollection = new MutableLiveData<>();
    public LiveData<Map<String, Integer>> currentPositionCollection = _currentPositionCollection;

    public void setCurrentPosition(String serviceName, int position) {
        _currentPosition.postValue(position);
        Map<String, Integer> value = _currentPositionCollection.getValue();
        if (value == null) {
            value = new HashMap<>();
        }
        if (serviceName != null && !"".equals(serviceName)) {
            value.put(serviceName, position);
            _currentPositionCollection.postValue(value);
        }
    }
    // ---------------------------------------------------------------------------------------------

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
