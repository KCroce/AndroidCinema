package com.example.androidfinal.login.dashboard.utils;

import android.os.Handler;
import android.os.Looper;

public class Debouncer {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;

    public void debounce(Runnable action, long delayMillis) {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        runnable = action;
        handler.postDelayed(runnable, delayMillis);
    }
}
