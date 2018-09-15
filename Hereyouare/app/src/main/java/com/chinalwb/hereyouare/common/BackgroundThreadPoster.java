package com.chinalwb.hereyouare.common;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class BackgroundThreadPoster extends HandlerThread {
    private static BackgroundThreadPoster mBackgroundThread;
    private static Handler mBackgroundHandler;

    private BackgroundThreadPoster(String name) {
        super(name);
    }

    private static BackgroundThreadPoster newInstance() {
        synchronized (BackgroundThreadPoster.class) {
            if (mBackgroundThread == null) {
                mBackgroundThread = new BackgroundThreadPoster("BackgroundThread");
                mBackgroundThread.start();
            }
        }

        return mBackgroundThread;
    }

    public static Looper getBackgroundLooper() {
        if (mBackgroundThread == null) {
            newInstance();
        }

        return mBackgroundThread.getLooper();
    }

    public static Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            Looper backLooper = getBackgroundLooper();
            mBackgroundHandler = new Handler(backLooper);
        }

        return mBackgroundHandler;
    }

    public static void post(Runnable runnable) {
        if (mBackgroundHandler == null) {
            getBackgroundHandler();
        }
        mBackgroundHandler.post(runnable);
    }
}
