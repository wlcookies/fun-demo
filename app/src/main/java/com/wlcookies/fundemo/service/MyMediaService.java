package com.wlcookies.fundemo.service;

import static androidx.media2.session.SessionCommand.COMMAND_CODE_PLAYER_PLAY;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.media.AudioAttributesCompat;
import androidx.media2.common.MediaItem;
import androidx.media2.common.MediaMetadata;
import androidx.media2.common.SessionPlayer;
import androidx.media2.common.UriMediaItem;
import androidx.media2.player.MediaPlayer;
import androidx.media2.session.MediaSession;
import androidx.media2.session.MediaSessionService;
import androidx.media2.session.SessionCommand;
import androidx.media2.session.SessionCommandGroup;
import androidx.media2.session.SessionResult;
import androidx.versionedparcelable.ParcelUtils;
import androidx.versionedparcelable.VersionedParcelable;

import com.wlcookies.fundemo.BuildConfig;

import java.util.List;
import java.util.concurrent.Executor;

public class MyMediaService extends MediaSessionService {

    public static final String TAG = MyMediaService.class.getSimpleName();

    private MediaSession mediaSession;

    private Executor callbackExecutor;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate: ---------------------------------------");
        callbackExecutor = ContextCompat.getMainExecutor(this);

        MediaPlayer mediaPlayer = new MediaPlayer(this);

        MediaMetadata mediaMetadata = new MediaMetadata.Builder()
                .putString(MediaMetadata.METADATA_KEY_MEDIA_URI, Uri.parse("file:///android_asset/" + "yxt.mp3").toString())
                .putLong(MediaMetadata.METADATA_KEY_DURATION, 210) // 播放时长
                .putString(MediaMetadata.METADATA_KEY_TITLE, "虞兮叹")
                .build();

        MediaItem mediaItem = new MediaItem.Builder()
                .setStartPosition(-1)
                .setEndPosition(-1)
                .setMetadata(mediaMetadata)
                .build();
        mediaPlayer.setMediaItem(mediaItem);

        mediaPlayer.setAudioAttributes(
                new AudioAttributesCompat.Builder()
                        .setUsage(AudioAttributesCompat.USAGE_MEDIA)
                        .setContentType(AudioAttributesCompat.CONTENT_TYPE_MUSIC)
                        .build()
        );

        mediaSession = new MediaSession.Builder(this, mediaPlayer)
                .setSessionCallback(callbackExecutor, new SessionCallback()).build();

        mediaPlayer.registerPlayerCallback(callbackExecutor, new SessionPlayer.PlayerCallback() {
            @Override
            public void onCurrentMediaItemChanged(@NonNull SessionPlayer player, @Nullable MediaItem item) {
                super.onCurrentMediaItemChanged(player, item);
                Log.d(TAG, "onCurrentMediaItemChanged: ---------------------------------------------");
            }

            @Override
            public void onPlaybackCompleted(@NonNull SessionPlayer player) {
                super.onPlaybackCompleted(player);
                Log.d(TAG, "onPlaybackCompleted: ---------------------------------------------------");
            }

            @Override
            public void onPlayerStateChanged(@NonNull SessionPlayer player, int playerState) {
                super.onPlayerStateChanged(player, playerState);
                Log.d(TAG, "onPlayerStateChanged: ---------------------------------------------------");
            }

            @Override
            public void onPlaylistMetadataChanged(@NonNull SessionPlayer player, @Nullable MediaMetadata metadata) {
                super.onPlaylistMetadataChanged(player, metadata);
                Log.d(TAG, "onPlaylistMetadataChanged: ---------------------------------------------------");
            }

            @Override
            public void onTrackSelected(@NonNull SessionPlayer player, @NonNull SessionPlayer.TrackInfo trackInfo) {
                super.onTrackSelected(player, trackInfo);
                Log.d(TAG, "onTrackSelected: ---------------------------------------------------");
            }

            @Override
            public void onTracksChanged(@NonNull SessionPlayer player, @NonNull List<SessionPlayer.TrackInfo> tracks) {
                super.onTracksChanged(player, tracks);
                Log.d(TAG, "onTracksChanged: -----------------------------------------------------");
            }

            @Override
            public void onTrackDeselected(@NonNull SessionPlayer player, @NonNull SessionPlayer.TrackInfo trackInfo) {
                super.onTrackDeselected(player, trackInfo);
                Log.d(TAG, "onTrackDeselected: -----------------------------------------------------");
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public ComponentName startForegroundService(Intent service) {
        return super.startForegroundService(service);
    }

    @Nullable
    @Override
    public MediaNotification onUpdateNotification(@NonNull MediaSession session) {
        return null;
    }

    @Nullable
    @Override
    public MediaSession onGetSession(@NonNull MediaSession.ControllerInfo controllerInfo) {
        return mediaSession;
    }

    static class SessionCallback extends MediaSession.SessionCallback {
        @Override
        public int onSetMediaUri(@NonNull MediaSession session, @NonNull MediaSession.ControllerInfo controller, @NonNull Uri uri, @Nullable Bundle extras) {
            Log.d(TAG, "onSetMediaUri: --------------------------------------");
            if (extras != null) {
                MediaMetadata mediaMetadata = ParcelUtils.getVersionedParcelable(extras, BuildConfig.APPLICATION_ID + ".MEDIA_ITEM");
                UriMediaItem mediaItem = new UriMediaItem.Builder(uri)
                        .setEndPosition(-1L)
                        .setMetadata(mediaMetadata)
                        .build();
                SessionPlayer player = session.getPlayer();
                player.addPlaylistItem(0, mediaItem);
                player.prepare();
                player.play();
            }
            return SessionResult.RESULT_SUCCESS;
        }

        @Nullable
        @Override
        public MediaItem onCreateMediaItem(@NonNull MediaSession session, @NonNull MediaSession.ControllerInfo controller, @NonNull String mediaId) {
            Log.d(TAG, "onCreateMediaItem: --------------------------------------");
            return super.onCreateMediaItem(session, controller, mediaId);
        }

        @Override
        public int onCommandRequest(@NonNull MediaSession session, @NonNull MediaSession.ControllerInfo controller, @NonNull SessionCommand command) {
            Log.d(TAG, "onCommandRequest: --------------------------------------");
            List<MediaItem> playlist = session.getPlayer().getPlaylist();
            Log.d(TAG, "onCommandRequest: " + playlist);
            if (session.getPlayer().getPlayerState() == SessionPlayer.PLAYER_STATE_ERROR) {
                session.getPlayer().prepare();
            }
            if (command.getCommandCode() == SessionCommand.COMMAND_CODE_PLAYER_PAUSE) {
//                log.derror("PAUSING IT DUDE!")
            }
            return super.onCommandRequest(session, controller, command);
        }

        @Nullable
        @Override
        public SessionCommandGroup onConnect(@NonNull MediaSession session, @NonNull MediaSession.ControllerInfo controller) {
            Log.d(TAG, "onConnect: --------------------------------------");
            return super.onConnect(session, controller);
        }

        @NonNull
        @Override
        public SessionResult onCustomCommand(@NonNull MediaSession session, @NonNull MediaSession.ControllerInfo controller, @NonNull SessionCommand customCommand, @Nullable Bundle args) {
            Log.d(TAG, "onCustomCommand: --------------------------------------");
            return super.onCustomCommand(session, controller, customCommand, args);
        }
    }
}