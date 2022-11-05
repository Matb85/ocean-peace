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

    /**
     *
     *
     * {@link java.lang.reflect.Array array} of {@link String String} containing PackageNames of apps selected for blocking in focus session.
     *
     * @since 5.11.2022, {@link FocusService FocusService} ver 2.0
     *
     * @see #isBlocked(String)
     */
    String[] appsPackageNames;

    /**
     *
     *
     * Function checking if provided <i>packageName</i> is in the {@link #appsPackageNames} array.
     * This is the basic form of search. Function is checking every element of {@link #appsPackageNames} if is equal to <i>packageName</i>.
     *
     * @param packageName {@link String String} containing packageName to search for
     * @return <i>true</i> - {@link #appsPackageNames} contains <i>packageName</i> <br> <i>false</i> - {@link #appsPackageNames} doesn't contain <i>packageName</i>
     *
     * @since 5.11.2022, {@link  FocusService FocusService} ver 2.0
     *
     * @see #appsPackageNames
     */
    boolean isBlocked(String packageName) {
        for (String i : appsPackageNames) {
            if (i.equals(packageName))
                return true;
        }
        return false;
    }

    /**
     *
     *
     * {@link Boolean Bolean} of current state of focus. <br>
     * If <i>true</i> focus session is currently running. <br>
     * If <i>false</i> there isn't any running focus session.
     *
     * @since 5.11.2022, {@link FocusService FocusService} ver 2.0
     *
     * @see FocusService
     */
    public boolean isRunning = false;

    /**
     *
     *
     * Stopwatch is a focus session type that runs endless until user stop it or process will be killed.
     * It blocks all preselected applications. Responsive for blocking is {@link com.redinn.oceanpeace.mayo.Mayo Mayo}.
     * Function first check if other focus session isn't running, then overrides <i>apps</i> array and send broadcast to Mayo to start blocking selected apps.
     *
     * @param apps {@link java.lang.reflect.Array array} of {@link String String} , String} containing PackageNames of apps selected for blocking in focus session
     * @return {@link androidx.work.ListenableWorker.Result Result}. If <i>failure</i>, a error message is provided. If <i>true</i> focus session started correctly
     *
     * @since 5.11.2022, {@link FocusService FocusService} ver 2.0
     *
     * @see #appsPackageNames
     * @see #startContinuous(String[], long)
     * @see #startPomodoro()
     * @see #stop()
     */
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

    /**
     *
     *
     * Continuous is a focus session that runs for chosen by user duration.
     * It blocks all preselected applications. Responsive for blocking is {@link com.redinn.oceanpeace.mayo.Mayo Mayo}.
     * Function first checks if other focus session isn't running, then overrides <i>apps</i> array and setups a handler's {@link Runnable Runnable} to run after timeout.
     * {@link Runnable Runable} runs {@link FocusService#stop() stop} function.
     * At the end function sends broadcast to Mayo to start blocking selected apps.
     *
     * @param apps {@link java.lang.reflect.Array array} of {@link String String} , String} containing PackageNames of apps selected for blocking in focus session
     * @param durationMillis duration of focus session provided in milliseconds
     * @return returns {@link androidx.work.ListenableWorker.Result Result}. If <i>failure</i>, a error message is provided. If <i>true</i> focus session started correctly
     *
     * @since 5.11.2022, {@link FocusService FocusService} ver 2.0
     *
     * @see #appsPackageNames
     * @see #startContinuous(String[], long)
     * @see #startPomodoro()
     * @see #stop()
     */
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