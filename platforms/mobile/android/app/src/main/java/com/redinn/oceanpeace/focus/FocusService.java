package com.redinn.oceanpeace.focus;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

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

    //TODO: add AlarmManager
    ListenableWorker.Result startContinuous(String[] apps, long timeMillis) {
        try {
            appsPackageNames = apps;

            broadcastStart();
        }
        catch (Exception e) {
            e.printStackTrace();
            return ListenableWorker.Result.failure();
        }

        isRunning = true;
        return ListenableWorker.Result.failure();
    }

    void startPomodoro() {

    }

    void stop() {
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