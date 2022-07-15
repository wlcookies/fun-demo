package com.wlcookies.commonmodule.media.client;

import android.content.Context;
import android.os.Handler;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.media2.session.MediaBrowser;
import androidx.media2.session.SessionResult;

import com.google.common.util.concurrent.ListenableFuture;
import com.wlcookies.commonmodule.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MediaSourceChangeClient implements IMediaController {

    private final Map<String, MediaClient> mMediaSourceClient = new HashMap<>();
    private final MediaClientViewModel mMediaClientViewModel;
    private final Context mContext;

    public MediaSourceChangeClient(Context context, List<String> mediaSourcePkgSet, MediaClientViewModel mediaClientViewModel) {
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
    public ListenableFuture<SessionResult> skipToPrevious(@Nullable String serviceName) {
        String currentMediaSource = serviceName != null ? serviceName : getCurrentMediaSource();
        MediaClient mediaClient = mMediaSourceClient.get(currentMediaSource);
        if (mediaClient != null) {
            return mediaClient.skipToPrevious();
        } else {
            return null;
        }
    }

    @Override
    public @Nullable
    ListenableFuture<SessionResult> skipToNext(@Nullable String serviceName) {
        String currentMediaSource = serviceName != null ? serviceName : getCurrentMediaSource();
        MediaClient mediaClient = mMediaSourceClient.get(currentMediaSource);
        if (mediaClient != null) {
            return mediaClient.skipToNext();
        } else {
            return null;
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

    }

    public void logMediaSessionSupportList() {
        String currentMediaSource = getCurrentMediaSource();
        MediaClient mediaClient = mMediaSourceClient.get(currentMediaSource);
        if (mediaClient != null) {
            mediaClient.logMediaSessionSupportList();
        }
    }

    public String getCurrentMediaSource() {
        if (mMediaClientViewModel != null) {
            return mMediaClientViewModel.currentPlayingComponentName.getValue() == null ? "" : mMediaClientViewModel.currentPlayingComponentName.getValue();
        } else {
            return "";
        }
    }
}
