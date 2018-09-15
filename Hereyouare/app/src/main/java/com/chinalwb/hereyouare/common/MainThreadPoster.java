package com.chinalwb.hereyouare.common;

import android.os.Handler;
import android.os.Looper;

public class MainThreadPoster {

    private static Handler sHandler;

    public static void post(Runnable runnable) {
        if (sHandler == null) {
            Looper mainLooper = Looper.getMainLooper();
            sHandler = new Handler(mainLooper);
        }
        sHandler.post(runnable);
    }
}
