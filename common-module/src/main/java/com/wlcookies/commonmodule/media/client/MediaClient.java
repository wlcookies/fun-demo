package com.wlcookies.commonmodule.media.client;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.media2.common.MediaItem;
import androidx.media2.common.MediaMetadata;
import androidx.media2.common.SessionPlayer;
import androidx.media2.session.MediaBrowser;
import androidx.media2.session.MediaController;
import androidx.media2.session.MediaSessionManager;
import androidx.media2.session.SessionCommandGroup;
import androidx.media2.session.SessionResult;
import androidx.media2.session.SessionToken;

import com.google.common.util.concurrent.ListenableFuture;
import com.wlcookies.commonmodule.utils.LogUtils;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Media2 Client
 *
 * @author wg
 * @version 1.0
 */
public class MediaClient {

    private final Executor mMainExecutor;
    private final SessionToken mAvailableToken;

    public MediaBrowser getMediaController() {
        return mMediaController;
    }

    private MediaBrowser mMediaController;

    private final Context mContext;
    private final String mServiceName;
    private final MediaSessionManager mMediaSessionManager;

    private final Handler progressHandler = new Handler(Looper.getMainLooper());
    private final Handler reConnectHandler = new Handler(Looper.getMainLooper());

    private Runnable mProgressRunnable;

    private boolean isSeeking = false;

    private final MediaClientViewModel mMediaClientViewModel;

    private final Bundle mConnectionHints;

    public MediaClient(@NonNull Context context, @NonNull String serviceName, MediaClientViewModel mediaClientViewModel, Bundle connectionHints) {

        this.mContext = context;
        this.mServiceName = serviceName;
        this.mMediaClientViewModel = mediaClientViewModel;
        this.mConnectionHints = connectionHints;

        mMainExecutor = ContextCompat.getMainExecutor(context);

        mMediaSessionManager = MediaSessionManager.getInstance(mContext);

        mAvailableToken = getAvailableToken(serviceName);
        //
        if (mAvailableToken != null) {

            createMediaController();

            mMediaClientViewModel.setInitMediaBrowserResult(serviceName, true);

            mProgressRunnable = () -> {
                if (!isSeeking) {
                    setCurrentPosition((int) getCurrentPosition());
                }
                progressHandler.removeCallbacks(mProgressRunnable);
                if (getPlayerState() == SessionPlayer.PLAYER_STATE_PLAYING) {
                    progressHandler.postDelayed(mProgressRunnable, 1000L);
                }
            };
        } else {
            LogUtils.e("MediaClient: Not Found MediaSession Service");
            mMediaClientViewModel.setInitMediaBrowserResult(serviceName, false);
        }
    }

    private void createMediaController() {
        MediaBrowser.Builder builder = new MediaBrowser.Builder(mContext)
                .setControllerCallback(mMainExecutor, new ControllerCallback())
                .setSessionToken(mAvailableToken);
        if (mConnectionHints != null) {
            builder.setConnectionHints(mConnectionHints);
        }
        mMediaController = builder.build();
    }

    public boolean isSeeking() {
        return isSeeking;
    }

    public void updatePosition() {
        progressHandler.postDelayed(mProgressRunnable, 1000L);
    }

    /**
     * setting seek bar
     *
     * @param mSeekBar seek bar view
     */
    public void setSeekBar(SeekBar mSeekBar) {
        if (mSeekBar != null) {
            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (isSeeking) {
                        setCurrentPosition(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    isSeeking = true;
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    isSeeking = false;
                    seekTo(seekBar.getProgress());
                }
            });
        }
    }

    /**
     * Control Play
     */
    public void play() {
        if (mMediaController != null && getPlayerState() == SessionPlayer.PLAYER_STATE_PAUSED) {
            LogUtils.d("执行播放");
            mMediaController.play();
        }
    }

    /**
     * Control Pause
     */
    public void pause() {
        if (mMediaController != null && getPlayerState() == SessionPlayer.PLAYER_STATE_PLAYING) {
            LogUtils.d("执行暂停");
            mMediaController.pause();
        }
    }

    /**
     * Control SeekTo
     */
    public void seekTo(long position) {
        if (mMediaController != null) {
            setCurrentPosition((int) position);
            mMediaController.seekTo(position);
        }
    }

    /**
     * Control Next Play
     */
    public ListenableFuture<SessionResult> skipToNext() {
        if (mMediaController != null) {
            LogUtils.d("执行下一曲");
            return mMediaController.skipToNextPlaylistItem();
        } else {
            return null;
        }
    }

    /**
     * Control Previous Play
     */
    public ListenableFuture<SessionResult> skipToPrevious() {
        if (mMediaController != null) {
            LogUtils.d("执行上一曲");
            return mMediaController.skipToPreviousPlaylistItem();
        } else {
            return null;
        }
    }

    /**
     * Get Current Position
     */
    public long getCurrentPosition() {
        return mMediaController.getCurrentPosition();
    }

    /**
     * Close
     */
    public void close() {
        try {
            if (mMediaController != null) {
                progressHandler.removeCallbacks(this::updatePosition);
                mMediaController.close();
            }
        } catch (Exception e) {
            LogUtils.e("MediaClient close: " + e.getMessage());
        }
    }

    /**
     * Get Connect State
     *
     * @return true Connected, false Disconnect
     */
    public boolean isConnected() {
        boolean isConnected = false;
        if (mMediaController != null) {
            isConnected = mMediaController.isConnected();
        }
        return isConnected;
    }

    /**
     * get play state
     *
     * @return {@link SessionPlayer#PLAYER_STATE_IDLE},{@link SessionPlayer#PLAYER_STATE_PLAYING},{@link SessionPlayer#PLAYER_STATE_PAUSED},{@link SessionPlayer#PLAYER_STATE_ERROR},
     */
    public int getPlayerState() {
        return mMediaController != null ? mMediaController.getPlayerState() : SessionPlayer.PLAYER_STATE_ERROR;
    }

    /**
     * Query this available tokens through this package
     *
     * @param serviceName service name
     * @return available SessionToken may by null
     */
    private SessionToken getAvailableToken(@NonNull String serviceName) {
        if (mContext != null && mMediaSessionManager != null) {
            for (SessionToken serviceToken : mMediaSessionManager.getSessionServiceTokens()) {
                if (serviceName.equals(serviceToken.getServiceName())) {
                    return serviceToken;
                }
            }
        }
        return null;
    }

    /**
     * Controller call back
     */
    private class ControllerCallback extends MediaBrowser.BrowserCallback {

        @Override
        public void onConnected(@NonNull MediaController controller, @NonNull SessionCommandGroup allowedCommands) {
            super.onConnected(controller, allowedCommands);
            LogUtils.d("MediaSession - onConnected " + controller.getConnectedToken());

            // setting connect state
            isConnected(true);
            // setting play state
            setPlayState(controller);
            // update play progress
            updatePosition();
        }

        @Override
        public void onPlayerStateChanged(@NonNull MediaController controller, int state) {
            super.onPlayerStateChanged(controller, state);
            // setting connect state
            setPlayState(controller);
            // update play progress
            updatePosition();

            LogUtils.d("MediaSession - onPlayerStateChanged");
        }

        @Override
        public void onCurrentMediaItemChanged(@NonNull MediaController controller, @Nullable MediaItem item) {
            super.onCurrentMediaItemChanged(controller, item);
            if (item != null) {
                MediaMetadata metadata = item.getMetadata();
                if (metadata != null) {
                    if (metadata.containsKey(MediaMetadata.METADATA_KEY_DURATION)) {
                        // update current media item
                        setCurrentMediaItem(metadata);
                    }
                }
            } else {
                LogUtils.d("MediaSession - onCurrentMediaItemChanged null");
            }
        }

        @Override
        public void onPlaybackCompleted(@NonNull MediaController controller) {
            super.onPlaybackCompleted(controller);

            LogUtils.d("MediaSession - onPlaybackCompleted");

            if (mMediaClientViewModel != null) {
                // update current playing Component
//                LogUtils.d("MediaSession - 设置播放状态 " + controller.getPlayerState());
                mMediaClientViewModel.setPlayState(mServiceName, SessionPlayer.PLAYER_STATE_PAUSED);
            }
        }

        @Override
        public void onTracksChanged(@NonNull MediaController controller, @NonNull List<SessionPlayer.TrackInfo> tracks) {
            super.onTracksChanged(controller, tracks);
        }

        @Override
        public void onDisconnected(@NonNull MediaController controller) {
            super.onDisconnected(controller);
            LogUtils.d("MediaSession - onDisconnected");
            isConnected(false);

            // 发起重连操作
            reConnectHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogUtils.d("MediaSession - 正在尝试重新连接 " + mServiceName);
                    createMediaController();
                }
            }, 1000 * 10);
        }

        // ---------------------------------------------------------------------------------

        /**
         * BufferingStateChanged
         *
         * @param controller controller
         * @param item       current media item
         * @param state      buffering state
         *                   {@link SessionPlayer#BUFFERING_STATE_UNKNOWN},
         *                   {@link SessionPlayer#BUFFERING_STATE_BUFFERING_AND_PLAYABLE},
         *                   {@link SessionPlayer#BUFFERING_STATE_BUFFERING_AND_STARVED},
         *                   {@link SessionPlayer#BUFFERING_STATE_COMPLETE},
         */
        @Override
        public void onBufferingStateChanged(@NonNull MediaController controller, @NonNull MediaItem item, int state) {
            super.onBufferingStateChanged(controller, item, state);
        }

        /**
         * SeekCompleted
         *
         * @param controller controller
         * @param position   current position
         */
        @Override
        public void onSeekCompleted(@NonNull MediaController controller, long position) {
            super.onSeekCompleted(controller, position);
        }

        /**
         * PlaylistChanged
         *
         * @param controller controller
         * @param list       new media list
         * @param metadata   new metadata
         */
        @Override
        public void onPlaylistChanged(@NonNull MediaController controller, @Nullable List<MediaItem> list, @Nullable MediaMetadata metadata) {
            super.onPlaylistChanged(controller, list, metadata);
        }

        /**
         * PlaylistMetadataChanged
         *
         * @param controller controller
         * @param metadata   new metadata
         */
        @Override
        public void onPlaylistMetadataChanged(@NonNull MediaController controller, @Nullable MediaMetadata metadata) {
            super.onPlaylistMetadataChanged(controller, metadata);
        }
    }


    /**
     * Logs supporting MediaSession are displayed
     */
    public void logMediaSessionSupportList() {
        if (mMediaSessionManager != null) {
            for (SessionToken serviceToken : mMediaSessionManager.getSessionServiceTokens()) {
                LogUtils.d("Support MediaSession --- PackageName：" + serviceToken.getPackageName() + " --- ServiceName：" + serviceToken.getServiceName() + " --- Type：" + serviceToken.getType());
            }
        }
    }

    /**
     * setting play state
     *
     * @param controller controller
     */
    private void setPlayState(MediaController controller) {
        if (mMediaClientViewModel != null && controller != null) {
            // update current playing Component
            if (controller.getPlayerState() == SessionPlayer.PLAYER_STATE_PLAYING) {
                setCurrentPlayingComponentName(mServiceName);
            } else {
                String currentPlayingComponentName = getCurrentPlayingComponentName();
                if (currentPlayingComponentName.equals(mServiceName)) {
                    setCurrentPlayingComponentName("");
                }
            }
            mMediaClientViewModel.setPlayState(mServiceName, controller.getPlayerState());
            LogUtils.d("MediaSession - setPlayState" + controller.getPlayerState());
        }
    }

    /**
     * setting connected state
     *
     * @param isConnected true or false
     */
    private void isConnected(Boolean isConnected) {
        if (mMediaClientViewModel != null) {
            mMediaClientViewModel.setConnectState(mServiceName, isConnected);
        }
        LogUtils.d("MediaSession - isConnected " + isConnected);
    }

    /**
     * setting current mediaMetadata
     *
     * @param mediaMetadata mediaMetadata
     */
    private void setCurrentMediaItem(MediaMetadata mediaMetadata) {
        if (mMediaClientViewModel != null) {
            LogUtils.d("MediaSession - setCurrentMediaItem " + mediaMetadata.toString());
            mMediaClientViewModel.setCurrentMediaItem(mServiceName, mediaMetadata);
        }
    }

    /**
     * setting current position
     *
     * @param position current position
     */
    private void setCurrentPosition(int position) {
        if (mMediaClientViewModel != null) {
            mMediaClientViewModel.setCurrentPosition(mServiceName, position);
        }
    }

    /**
     * setting current playing media session service package name
     *
     * @param pkgName service package name
     */
    private void setCurrentPlayingComponentName(String pkgName) {
        if (mMediaClientViewModel != null) {
            mMediaClientViewModel.setCurrentPlayingComponentName(pkgName);
        }
    }

    private String getCurrentPlayingComponentName() {
        if (mMediaClientViewModel != null) {
            return mMediaClientViewModel.currentPlayingComponentName.getValue() == null ? "" : mMediaClientViewModel.currentPlayingComponentName.getValue();
        } else {
            return "";
        }
    }
}
