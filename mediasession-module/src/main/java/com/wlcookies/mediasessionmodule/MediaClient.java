package com.wlcookies.mediasessionmodule;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.media2.common.MediaItem;
import androidx.media2.common.MediaMetadata;
import androidx.media2.common.SessionPlayer;
import androidx.media2.session.MediaBrowser;
import androidx.media2.session.MediaController;
import androidx.media2.session.MediaSessionManager;
import androidx.media2.session.SessionCommand;
import androidx.media2.session.SessionCommandGroup;
import androidx.media2.session.SessionResult;
import androidx.media2.session.SessionToken;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * Media2 Client
 *
 * @author wg
 * @version 1.0
 */
public class MediaClient {

    public static boolean isDebug = true;

    private MediaBrowser mMediaController;
    private static final String TAG = "MediaClient";

    private final Executor mMainExecutor;
    private final Context mContext;
    private final MediaSessionManager mMediaSessionManager;

    private final Handler progressHandler = new Handler(Looper.getMainLooper());
    public static boolean isSeeking = false;

    private MediaClientViewModel mMediaClientViewModel;

    public MediaClient(@NonNull Context context, @NonNull String packageName, Bundle connectionHints) {

        this.mContext = context;

        mMainExecutor = ContextCompat.getMainExecutor(context);

        mMediaSessionManager = MediaSessionManager.getInstance(mContext);

        SessionToken availableToken = getAvailableToken(packageName);

        if (availableToken != null) {
            MediaBrowser.Builder builder = new MediaBrowser.Builder(context)
                    .setControllerCallback(mMainExecutor, new ControllerCallback())
                    .setSessionToken(availableToken);
            if (connectionHints != null) {
                builder.setConnectionHints(connectionHints);
            }
            mMediaController = builder.build();
        } else {
//            log("未发现此应用中的媒体服务");
            Log.e(TAG, "MediaClient: 未发现此应用中的媒体服务");
        }
    }

    public void updatePosition() {
        if (!isSeeking) {
            // 正在播放的情况下，防止拖拽过程中更新位置
            setCurrentPosition((int) getCurrentPosition());
        }
        if (getPlayerState() == SessionPlayer.PLAYER_STATE_PLAYING) {
            progressHandler.postDelayed(this::updatePosition, 1000L);
        } else {
            progressHandler.removeCallbacks(this::updatePosition);
        }
    }

    /**
     * 进度搜索
     *
     * @param mSeekBar 进度搜索
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
     * 开始播放
     */
    public void play() {
        if (mMediaController != null && getPlayerState() == SessionPlayer.PLAYER_STATE_PAUSED) {
            mMediaController.play();
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mMediaController != null && getPlayerState() == SessionPlayer.PLAYER_STATE_PLAYING) {
            mMediaController.pause();
        }
    }

    /**
     * 从给定位置播放
     */
    public void seekTo(long position) {
        if (mMediaController != null) {
            setCurrentPosition((int) position);
            ListenableFuture<SessionResult> seekToResult = mMediaController.seekTo(position);
            seekToResult.addListener(() -> {
                try {
                    SessionResult sessionResult = seekToResult.get(3000L, TimeUnit.SECONDS);
                    log("寻找位置 0 == " + sessionResult.getResultCode());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, mMainExecutor);

        }
    }

    /**
     * 下一首
     */
    public void skipToNext() {
        if (mMediaController != null) {
            setCurrentPosition(0);
            mMediaController.skipToNextPlaylistItem();
        }
    }

    /**
     * 上一首
     */
    public void skipToPrevious() {
        if (mMediaController != null) {
            setCurrentPosition(0);
            mMediaController.skipToPreviousPlaylistItem();
        }
    }

    /**
     * 当前播放位置
     */
    public long getCurrentPosition() {
        long position = 0;
        Integer currentPositionValue = mMediaClientViewModel.currentPosition.getValue();
        if (currentPositionValue != null) {
            position = currentPositionValue;
        }
        if (mMediaController != null && getPlayerState() == SessionPlayer.PLAYER_STATE_PLAYING) {
            position = mMediaController.getCurrentPosition();
        }
        return position;
    }

    /**
     * 关闭
     */
    public void close() {
        if (mMediaController != null) {
            mMediaController.close();
            progressHandler.removeCallbacks(this::updatePosition);
        }
    }

    /**
     * 获取连接状态
     *
     * @return true 已连接，false 未连接
     */
    public boolean isConnected() {
        boolean isConnected = false;
        if (mMediaController != null) {
            isConnected = mMediaController.isConnected();
        }
        return isConnected;
    }

    /**
     * 获取播放状态
     *
     * @return {@link SessionPlayer#PLAYER_STATE_IDLE},{@link SessionPlayer#PLAYER_STATE_PLAYING},{@link SessionPlayer#PLAYER_STATE_PAUSED},{@link SessionPlayer#PLAYER_STATE_ERROR},
     */
    public int getPlayerState() {
        return mMediaController != null ? mMediaController.getPlayerState() : SessionPlayer.PLAYER_STATE_ERROR;
    }

    /**
     * 根据服务端包名获取可用的SessionToken
     *
     * @param packageName 服务端包名
     * @return 可用的SessionToken，可能为null
     */
    private SessionToken getAvailableToken(String packageName) {
        if (mContext != null && mMediaSessionManager != null) {
            for (SessionToken serviceToken : mMediaSessionManager.getSessionServiceTokens()) {
                if (serviceToken.getPackageName().equals(packageName)) {
                    return serviceToken;
                }
            }
        }
        return null;
    }

    /**
     * 提供数据视图给外部使用
     *
     * @param viewModelStoreOwner 所有者
     * @return MediaClientViewModel
     */
    public MediaClientViewModel getDataViewModel(ViewModelStoreOwner viewModelStoreOwner) {
        if (mMediaClientViewModel == null) {
            mMediaClientViewModel = new ViewModelProvider(viewModelStoreOwner).get(MediaClientViewModel.class);
        }
        return mMediaClientViewModel;
    }

    /**
     * 客户端回调信息处理
     */
    private class ControllerCallback extends MediaBrowser.BrowserCallback {

        @Override
        public void onConnected(@NonNull MediaController controller, @NonNull SessionCommandGroup allowedCommands) {
            super.onConnected(controller, allowedCommands);
            // 设置连接状态
            isConnected(true);
            // 设置播放状态
            setPlayState(controller);
            //
            updatePosition();
        }

        @Override
        public void onPlayerStateChanged(@NonNull MediaController controller, int state) {
            super.onPlayerStateChanged(controller, state);
            // 设置播放状态
            setPlayState(controller);
            // 更新位置进度
            updatePosition();
        }

        @Override
        public void onCurrentMediaItemChanged(@NonNull MediaController controller, @Nullable MediaItem item) {
            super.onCurrentMediaItemChanged(controller, item);
            if (item != null) {
                MediaMetadata metadata = item.getMetadata();
                if (metadata != null) {
                    // 更新当前播放媒体
                    setCurrentMediaItem(metadata);
                }
            }
        }

        @Override
        public void onDisconnected(@NonNull MediaController controller) {
            super.onDisconnected(controller);
            // 断开连接
            isConnected(false);
        }

        // ---------------------------------------------------------------------------------华丽分割线

        /**
         * 缓冲事件
         *
         * @param controller 控制器
         * @param item       当前播放媒体
         * @param state      缓冲状态
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
         * Seek to 已经完成
         *
         * @param controller 控制器
         * @param position   位置
         */
        @Override
        public void onSeekCompleted(@NonNull MediaController controller, long position) {
            super.onSeekCompleted(controller, position);
        }

        /**
         * 播放列表更改
         *
         * @param controller 控制器
         * @param list       新的媒体列表
         * @param metadata   新的元数据
         */
        @Override
        public void onPlaylistChanged(@NonNull MediaController controller, @Nullable List<MediaItem> list, @Nullable MediaMetadata metadata) {
            super.onPlaylistChanged(controller, list, metadata);
        }

        /**
         * 播放列表元数据更改
         *
         * @param controller 控制器
         * @param metadata   新媒体元数据
         */
        @Override
        public void onPlaylistMetadataChanged(@NonNull MediaController controller, @Nullable MediaMetadata metadata) {
            super.onPlaylistMetadataChanged(controller, metadata);
        }
    }

    /**
     * 打印调试日志
     *
     * @param object 日志信息
     */
    private static void log(@Nullable Object object) {
        if (isDebug && object != null) {
            Log.d(TAG, object.toString());
        }
    }

    /**
     * 打印本地支持的MediaSession组件
     */
    public void logMediaSessionSupportList() {
        if (mMediaSessionManager != null) {
            for (SessionToken serviceToken : mMediaSessionManager.getSessionServiceTokens()) {
                log("支持的MediaSession --- PackageName：" + serviceToken.getPackageName() + " --- ServiceName：" + serviceToken.getServiceName() + " --- Type：" + serviceToken.getType());
            }
        }
    }

    /**
     * 设置播放状态
     *
     * @param controller 控制器
     */
    private void setPlayState(MediaController controller) {
        if (mMediaClientViewModel != null && controller != null) {
            mMediaClientViewModel.setPlayState(controller.getPlayerState());
        }
    }

    /**
     * 设置连接状态
     *
     * @param isConnected 控制器
     */
    private void isConnected(Boolean isConnected) {
        if (mMediaClientViewModel != null) {
            mMediaClientViewModel.setConnectState(isConnected);
        }
    }

    /**
     * 设置当前播放媒体
     *
     * @param mediaMetadata 当前媒体数据
     */
    private void setCurrentMediaItem(MediaMetadata mediaMetadata) {
        if (mMediaClientViewModel != null) {
            mMediaClientViewModel.setCurrentMediaItem(mediaMetadata);
        }
    }

    /**
     * 设置当前播放进度
     *
     * @param position 当前媒体数据
     */
    private void setCurrentPosition(int position) {
        if (mMediaClientViewModel != null) {
            mMediaClientViewModel.setCurrentPosition(position);
        }
    }
}
