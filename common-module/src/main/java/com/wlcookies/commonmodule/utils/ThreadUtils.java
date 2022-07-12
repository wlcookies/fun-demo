package com.wlcookies.commonmodule.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public final class ThreadUtils {

    private static final class UiThread {
        public static final Handler handler = new Handler(Looper.getMainLooper());
    }

    private static final class WorkThread {
        private static final Handler handler;

        static {
            HandlerThread handlerThread = new HandlerThread("COMMON_WORKER_THREAD");
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
        }
    }

    public static void runOnUiThread(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            UiThread.handler.post(runnable);
        }
    }

    public static void runOnWorkThread(Runnable runnable) {
        runOnWorkThread(runnable, 0);
    }

    public static void runOnWorkThread(Runnable runnable, long delayMillis) {
        WorkThread.handler.removeCallbacks(runnable);
        if (delayMillis > 0) {
            WorkThread.handler.postDelayed(runnable, delayMillis);
        } else if (Looper.myLooper() == WorkThread.handler.getLooper()) {
            runnable.run();
        } else {
            WorkThread.handler.post(runnable);
        }
    }

    public static void destroy() {
        WorkThread.handler.removeCallbacksAndMessages(null);
    }
}