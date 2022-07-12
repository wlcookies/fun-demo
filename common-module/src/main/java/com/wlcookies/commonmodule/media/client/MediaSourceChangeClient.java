package com.wlcookies.commonmodule.media.client;

import android.content.Context;
import android.os.Handler;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.media2.session.MediaBrowser;

import com.wlcookies.commonmodule.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MediaSourceChangeClient implements IMediaController {

    private final Map<String, MediaClient> mMediaSourceClient = new HashMap<>();
    private final MediaClientViewModel mMediaClientViewModel;

    private final List<String> mNeedReconnectList = new ArrayList<>(); // 用于重连的集合

    private final Context mContext;

    private final Handler mReconnectHandler = new Handler();

    private final Runnable mReconnectRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.d("正在尝试重新连接媒体服务：" + mNeedReconnectList);
                        ArrayList<String> lll = new ArrayList<>(mNeedReconnectList);
                        for (String serviceName : lll) {
                            MediaClient mediaClient = new MediaClient(mContext, serviceName, mMediaClientViewModel, null);
                            mMediaSourceClient.put(serviceName, mediaClient);
                        }
                    }
                }).start();

            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.d(e.getMessage());
            }
        }
    };

    public MediaSourceChangeClient(Context context, Set<String> mediaSourcePkgSet, MediaClientViewModel mediaClientViewModel) {
        this.mContext = context;
        this.mMediaClientViewModel = mediaClientViewModel;
        for (String serviceName : mediaSourcePkgSet) {
            if (serviceName != null && !"".equals(serviceName)) {
                MediaClient mediaClient = new MediaClient(context, serviceName, mediaClientViewModel, null);
                mMediaSourceClient.put(serviceName, mediaClient);
            }
        }
    }

    @Override
    public void pause(@Nullable String serviceName) {
        String currentMediaSource = serviceName != null ? serviceName : getCurrentMediaSource();
        MediaClient mediaClient = mMediaSourceClient.get(currentMediaSource);
        if (mediaClient != null) {
            mediaClient.pause();
        }
    }

    @Override
    public void play(@Nullable String serviceName) {
        String currentMediaSource = serviceName != null ? serviceName : getCurrentMediaSource();
        MediaClient mediaClient = mMediaSourceClient.get(currentMediaSource);
        if (mediaClient != null) {
            mediaClient.play();
        }
    }

    @Override
    public void skipToPrevious(@Nullable String serviceName) {
        String currentMediaSource = serviceName != null ? serviceName : getCurrentMediaSource();
        MediaClient mediaClient = mMediaSourceClient.get(currentMediaSource);
        if (mediaClient != null) {
            mediaClient.skipToPrevious();
        }
    }

    @Override
    public void skipToNext(@Nullable String serviceName) {
        String currentMediaSource = serviceName != null ? serviceName : getCurrentMediaSource();
        MediaClient mediaClient = mMediaSourceClient.get(currentMediaSource);
        if (mediaClient != null) {
            mediaClient.skipToNext();
        }
    }

    @Override
    public void seekTo(@Nullable String serviceName, long position) {
        String currentMediaSource = serviceName != null ? serviceName : getCurrentMediaSource();
        MediaClient mediaClient = mMediaSourceClient.get(currentMediaSource);
        if (mediaClient != null) {
            mediaClient.seekTo(position);
        }
    }

    @Override
    public MediaBrowser getMediaBrowser(@Nullable String serviceName) {
        String currentMediaSource = serviceName != null ? serviceName : getCurrentMediaSource();
        MediaClient mediaClient = mMediaSourceClient.get(currentMediaSource);
        if (mediaClient != null) {
            return mediaClient.getMediaController();
        }
        return null;
    }

    @Override
    public boolean isSeeking(@Nullable String serviceName) {
        String currentMediaSource = serviceName != null ? serviceName : getCurrentMediaSource();
        MediaClient mediaClient = mMediaSourceClient.get(currentMediaSource);
        if (mediaClient != null) {
            return mediaClient.isSeeking();
        }
        return false;
    }

    @Override
    public void setSeekBar(@Nullable String serviceName, SeekBar seekBar) {
        String currentMediaSource = serviceName != null ? serviceName : getCurrentMediaSource();
        MediaClient mediaClient = mMediaSourceClient.get(currentMediaSource);
        if (mediaClient != null) {
            mediaClient.setSeekBar(seekBar);
        }
    }

    @Override
    public void close(@Nullable String serviceName) {
        if (serviceName == null) {
            // all close
            mMediaSourceClient.forEach((s, mediaClient) -> mediaClient.close());
            mReconnectHandler.removeCallbacks(mReconnectRunnable);
        } else {
            MediaClient mediaClient = mMediaSourceClient.get(serviceName);
            if (mediaClient != null) {
                mediaClient.close();
            }
        }
    }

    /**
     * 重连
     *
     * @param needReconnect 需要重连的组件
     */
    @Override
    public synchronized void reconnect(Map<String, Boolean> needReconnect) {
        // 1. 分离 needReconnect 中，value 是 false 的 Key
        // 2. 判断 key 是不是在 mNeedReconnectList 中存在
        // 3. 执行延迟操作
        for (String serviceNameKey : needReconnect.keySet()) {
            Boolean connectResult = needReconnect.get(serviceNameKey);
            if (connectResult != null) {
                boolean contains = mNeedReconnectList.contains(serviceNameKey);
                if (connectResult) {
                    if (contains) {
                        mNeedReconnectList.remove(serviceNameKey);
                    }
                } else {
                    if (!contains) {
                        mNeedReconnectList.add(serviceNameKey);
                    }
                }
            }
        }
        mReconnectHandler.removeCallbacks(mReconnectRunnable);
        if (mNeedReconnectList.size() > 0) {
            // 重连延迟间隔
            long NEED_RECONNECT_DELAY = 10 * 1000;
            mReconnectHandler.postDelayed(mReconnectRunnable, NEED_RECONNECT_DELAY);
        }
    }

    public void logMediaSessionSupportList() {
        String currentMediaSource = getCurrentMediaSource();
        MediaClient mediaClient = mMediaSourceClient.get(currentMediaSource);
        if (mediaClient != null) {
            mediaClient.logMediaSessionSupportList();
        }
    }

    private String getCurrentMediaSource() {
        if (mMediaClientViewModel != null) {
            return mMediaClientViewModel.currentPlayingComponentName.getValue() == null ? "" : mMediaClientViewModel.currentPlayingComponentName.getValue();
        } else {
            return "";
        }
    }
}
