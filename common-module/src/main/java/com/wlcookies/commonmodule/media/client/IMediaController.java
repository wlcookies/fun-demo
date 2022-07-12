package com.wlcookies.commonmodule.media.client;

import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.media2.session.MediaBrowser;

import java.util.Map;

public interface IMediaController {

    void pause(@Nullable String serviceName);

    void play(@Nullable String serviceName);

    void skipToPrevious(@Nullable String serviceName);

    void skipToNext(@Nullable String serviceName);

    void seekTo(@Nullable String serviceName, long position);

    MediaBrowser getMediaBrowser(@Nullable String serviceName);

    boolean isSeeking(@Nullable String serviceName);

    void setSeekBar(@Nullable String serviceName, SeekBar seekBar);

    void close(@Nullable String serviceName);

    void reconnect(Map<String, Boolean> needReconnect);
}
