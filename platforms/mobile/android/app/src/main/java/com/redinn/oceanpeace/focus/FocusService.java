package com.redinn.oceanpeace.focus;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

import com.redinn.oceanpeace.R;

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
        handlerThread = new HandlerThread("ocean.focus.handler.thread");
        looper = handlerThread.getLooper();
        handlerThread.start();
        mHandler = new Handler();

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        handlerThread.quitSafely();

        notificationManager.deleteNotificationChannel("911");

        super.onDestroy();
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
     * Function first check if other focus session isn't running, then overrides {@link #appsPackageNames} array and send broadcast to Mayo to start blocking selected apps.
     *
     * @param apps {@link java.lang.reflect.Array array} of {@link String String} containing PackageNames of apps selected for blocking in focus session
     * @return {@link androidx.work.ListenableWorker.Result Result}. If <i>failure</i>, a error message is provided. If <i>true</i> focus session started correctly
     *
     * @since 5.11.2022, {@link FocusService FocusService} ver 2.0
     *
     * @see #appsPackageNames
     * @see #startContinuous(String[], long)
     * @see #startPomodoro(String[], long, long, int)
     * @see #stop()
     */
    ListenableWorker.Result startStopwatch(String[] apps) {
        // Check if the other session isn't running
        if (isRunning) {
            Data returnData = new Data.Builder().putString(FIELD_RESULT, ERROR_RUNNING).build();

            return ListenableWorker.Result.failure(returnData);
        }

        try {
            // override the array
            appsPackageNames = apps;

            //blocking notifications
            notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY);

            // send broadcast to Mayo
            broadcastStart();
        }
        catch (Exception e) {
            e.printStackTrace();
            Data returnData = new Data.Builder().putString(FIELD_RESULT, ERROR_DATA).build();
            return ListenableWorker.Result.failure(returnData);
        }

        isRunning = true;
        return ListenableWorker.Result.success();
    }

    /**
     *
     *
     * Continuous is a focus session that runs for chosen by user duration.
     * It blocks all preselected applications. Responsive for blocking is {@link com.redinn.oceanpeace.mayo.Mayo Mayo}.
     * Function first checks if other focus session isn't running, then overrides {@link #appsPackageNames} array and setups a handler's {@link Runnable Runnable} to run after timeout.
     * {@link Runnable Runable} runs {@link FocusService#stop() stop} function.
     * At the end function sends broadcast to Mayo to start blocking selected apps.
     *
     * @param apps {@link java.lang.reflect.Array array} of {@link String String} containing PackageNames of apps selected for blocking in focus session
     * @param durationMillis duration of focus session provided in milliseconds
     * @return returns {@link androidx.work.ListenableWorker.Result Result}. If <i>failure</i>, a error message is provided. If <i>true</i> focus session started correctly
     *
     * @since 5.11.2022, {@link FocusService FocusService} ver 2.0
     *
     * @see #appsPackageNames
     * @see #startContinuous(String[], long)
     * @see #startPomodoro(String[], long, long, int)
     * @see #stop()
     */
    ListenableWorker.Result startContinuous(String[] apps, long durationMillis) {
        // Check if the other focus session isn't running
        if (isRunning) {
            Data returnData = new Data.Builder().putString(FIELD_RESULT, ERROR_RUNNING).build();

            return ListenableWorker.Result.failure(returnData);
        }

        try {
            // override the array
            appsPackageNames = apps;

            //blocking notifications
            notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY);

            // post a handler Runnable to stop the session after timeout
            mHandler.postAtTime(
                    new Runnable() {
                        @Override
                        public void run() {
                            stop();
                        }
                    },
                    SystemClock.uptimeMillis() + durationMillis
            );

            // send broadcast to Mayo
            broadcastStart();
        }
        catch (Exception e) {
            e.printStackTrace();
            Data returnData = new Data.Builder().putString(FIELD_RESULT, ERROR_DATA).build();
            return ListenableWorker.Result.failure(returnData);
        }

        isRunning = true;
        return ListenableWorker.Result.success();
    }

    private int cyclesCount =1;

    /**
     * Pomodoro is a type of Focus session containing two phases running alternately: work and break. One full cycle consist of one work phase and one break phase,
     * except last work phase which after which there is no break phase and the session ends.
     * Function at the beginning checks if there is no other focus sessions running, then updates {@link #appsPackageNames} array,
     * blocks notifications and starts work phase {@link Runnable} which will handle the lifecycle of session.
     * After the specified number of cycles will end the function will call {@link #stop()} function.
     * @param apps {@link java.lang.reflect.Array array} of {@link String String} containing PackageNames of apps selected for blocking in focus session
     * @param workDurationInMills duration of work phases, provided in milliseconds
     * @param breakDurationInMills duration of break phases, provided in milliseconds
     * @param numberOfCycle number of cycles
     * @return {@link androidx.work.ListenableWorker.Result Result}. If <i>failure</i>, a error message is provided. If <i>true</i> focus session started correctly
     *
     * @since 9.11.2022, {@link FocusService FocusService} ver 2.0
     *
     * @see #appsPackageNames
     * @see #startContinuous(String[], long)
     * @see #startStopwatch(String[])
     * @see #stop()
     */
    ListenableWorker.Result startPomodoro(String[] apps, long workDurationInMills, long breakDurationInMills, int numberOfCycle) {

        // Check if the other session isn't running
        if (isRunning) {
            Data returnData = new Data.Builder().putString(FIELD_RESULT, ERROR_RUNNING).build();

            return ListenableWorker.Result.failure(returnData);
        }

        try {
            // override the array
            appsPackageNames = apps;


            setupWorkingPhase(workDurationInMills, breakDurationInMills, numberOfCycle);

            //blocking notifications
            notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY);

            // send broadcast to Mayo
            broadcastStart();
        }
        catch (Exception e) {
            e.printStackTrace();
            Data returnData = new Data.Builder().putString(FIELD_RESULT, ERROR_DATA).build();
            return ListenableWorker.Result.failure(returnData);
        }

        isRunning = true;
        return ListenableWorker.Result.success();

    }

    private void setupWorkingPhase(long workDuration, long breakDuration, long numberOfCycles) {
        // check if the index of cycle reached the limit
        if (cyclesCount >= numberOfCycles) {
            stop();
            return;
        }

        mHandler.postAtTime(
                new Runnable() {
                    @Override
                    public void run() {
                        // start break phase
                        setupBreakPhase(breakDuration, workDuration, numberOfCycles);
                    }
                },
                SystemClock.uptimeMillis() + workDuration
        );

    }

    private void setupBreakPhase(long breakDuration, long workDuration, long numberOfSessions) {
        mHandler.postAtTime(
                new Runnable() {
                    @Override
                    public void run() {
                        // index of incoming cycle
                        cyclesCount++;
                        //start work phase
                        setupWorkingPhase(workDuration, breakDuration, numberOfSessions);
                    }
                },
                SystemClock.uptimeMillis() + breakDuration
        );
    }


    public void stop() {
        // make array empty
        appsPackageNames = new String[]{};

        //blocking notifications
        notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
        notificationManager.cancelAll();

        // remove all queued handler Runnables
        mHandler.removeCallbacksAndMessages(null);

        isRunning = false;
        cyclesCount = 1;

        Intent stopFocus = new Intent();
        stopFocus.setAction("ocean.waves.mayo.focus.stop");
        getApplicationContext().sendBroadcast(stopFocus);

        stopForeground(true);
        stopSelf();
    }


    /**
     * Function sending broadcast for Mayo to bind this service
     */
    private void broadcastStart() {
        Intent runFocus = new Intent();
        runFocus.setAction("ocean.waves.mayo.focus.start");
        runFocus.putExtra("packages", appsPackageNames);
        getApplicationContext().sendBroadcast(runFocus);
    }



    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder implements IBinder {
        public FocusService getService() {
            // Return this instance of LocalService so clients can call public methods
            return FocusService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private NotificationManager notificationManager;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(new NotificationChannel("911", "oceanpeace.focus", NotificationManager.IMPORTANCE_HIGH));

        Intent notificationIntent = new Intent(this, FocusService.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE);

        Notification notification =
                new Notification.Builder(this, "911")
                        .setContentTitle("Focus session is running")
                        .setSmallIcon(R.drawable.splash)
                        //.setContentIntent(pendingIntent)
                        .build();

        startForeground(912, notification);

        return super.onStartCommand(intent, flags, startId);
    }
}
