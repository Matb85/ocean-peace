package com.redinn.oceanpeace.focus;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;

import androidx.work.Data;
import androidx.work.ListenableWorker;

/**
 * Finish focus:
 * TODO: create communication with MayoAPI
 * TODO: make notification
 * TODO: add continuous
 * TODO: add pomodoro
 * TODO: add stopwatch
 * TODO: run tests
 */
public class FocusService extends Service {
    public FocusService() {
    }

    public final static String ERROR_RUNNING = "Focus is running";
    public final static String ERROR_DATA = "Data error occurred";
    public final static String FIELD_RESULT = "result";


    HandlerThread handlerThread = new HandlerThread("ocean.focus.handler.thread");
    Looper looper = handlerThread.getLooper();
    Handler mHandler;

    @Override
    public void onCreate() {
        handlerThread.start();
        mHandler = new Handler(looper);

        super.onCreate();
    }

    String[] appsPackageNames;
    boolean isBlocked(String packageName) {
        for (String i : appsPackageNames) {
            if (i.equals(packageName))
                return true;
        }
        return false;
    }

    public boolean isRunning = false;


    ListenableWorker.Result startStopwatch(String[] apps) {
        if (isRunning) {
            Data returnData = new Data.Builder().putString(FIELD_RESULT, ERROR_RUNNING).build();

            return ListenableWorker.Result.failure(returnData);
        }

        try {
            appsPackageNames = apps;

            broadcastStart();
        }
        catch (Exception e) {
            e.printStackTrace();
            return ListenableWorker.Result.failure();
        }

        isRunning = true;
        return ListenableWorker.Result.success();
    }


    ListenableWorker.Result startContinuous(String[] apps, long durationMillis) {
        if (isRunning) {
            Data returnData = new Data.Builder().putString(FIELD_RESULT, ERROR_RUNNING).build();

            return ListenableWorker.Result.failure(returnData);
        }

        try {
            appsPackageNames = apps;

            mHandler.postAtTime(
                    new Runnable() {
                        @Override
                        public void run() {
                            stop();
                        }
                    },
                    SystemClock.uptimeMillis() + durationMillis
            );

            broadcastStart();
        }
        catch (Exception e) {
            e.printStackTrace();
            Data returnData = new Data.Builder().putString(FIELD_RESULT, ERROR_DATA).build();
            return ListenableWorker.Result.failure(returnData);
        }

        isRunning = true;
        return ListenableWorker.Result.failure();
    }

    void startPomodoro() {

    }

    
    public void stop() {
        appsPackageNames = new String[]{};

        mHandler.removeCallbacksAndMessages(null);

        isRunning = false;
    }



    private void broadcastStart() {
        Intent runFocus = new Intent();
        runFocus.setAction("ocean.waves.mayo.focus.start");
        getApplicationContext().sendBroadcast(runFocus);
    }



    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder implements IBinder {
        FocusService getService() {
            // Return this instance of LocalService so clients can call public methods
            return FocusService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}