package com.wlcookies.mediasessionmodule;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.media2.session.MediaBrowser;
import androidx.media2.session.MediaController;
import androidx.media2.session.MediaSessionManager;
import androidx.media2.session.SessionCommandGroup;
import androidx.media2.session.SessionToken;

import com.wlcookies.commonmodule.utils.DateUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Media2 客户端
 *
 * @author wg
 * @version 1.0
 */
public class MediaClient {

    private MediaBrowser mMediaController;
    private boolean isDebug = true;
    private static final String TAG = "MediaClient";

    private final Executor mMainExecutor;
    private final Context mContext;
    private final MediaSessionManager mMediaSessionManager;

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
            // TODO 没有发现可用的媒体服务

        }
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
     * 客户端回调信息处理
     */
    private class ControllerCallback extends MediaBrowser.BrowserCallback {
        @Override
        public void onConnected(@NonNull MediaController controller, @NonNull SessionCommandGroup allowedCommands) {
            super.onConnected(controller, allowedCommands);
            log("连接成功");
        }
    }

    /**
     * 打印调试日志
     *
     * @param object 日志信息
     */
    private void log(@NonNull Object object) {
        if (isDebug) {
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
}
