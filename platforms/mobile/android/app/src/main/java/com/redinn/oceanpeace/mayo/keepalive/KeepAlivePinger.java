package com.redinn.oceanpeace.mayo.keepalive;

import android.icu.util.Calendar;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

public class KeepAlivePinger {
    private static final String TAG = "MAPI";
    private static final String PING_TOKEN = "ping";
    HandlerThread pingThread = new HandlerThread("mayo.api.ping");
    Handler pingerHandler;
    Looper pingLooper;
    public KeepAlivePinger() {
        pingThread.start();
        pingLooper = pingThread.getLooper();
        pingerHandler = new Handler(pingLooper);
    }

    private boolean running = false;
    private void setRunning(boolean x) {
        running = x;
    }
    public boolean isRunning() {
        return running;
    }

    public void preparePing() {
        pingerHandler.post(ping);
        setRunning(true);
    }

    long lastTimePing = 0;

    Runnable ping = new Runnable() {
        @Override
        public void run() {
            lastTimePing = Calendar.getInstance().getTimeInMillis();
            Log.d(TAG, "ping at: " + Calendar.getInstance().getTime().toString());

            pingerHandler.postAtTime(
                    this,
                    PING_TOKEN,
                    SystemClock.uptimeMillis() + (6 * 60 * 1000)
            );
        }
    };


    public void stop() {
        setRunning(false);
        pingerHandler.removeCallbacksAndMessages(PING_TOKEN);
        pingLooper.quitSafely();
        pingThread.quitSafely();
    }
}
