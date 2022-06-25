package com.oceanpeace.redinn.focus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.oceanpeace.redinn.Broadcast;
import com.oceanpeace.redinn.MainActivity;

import java.util.Calendar;

public class Focus extends Service {
    private static Focus instance;

    public static Focus getInstance()
    {
        if (instance == null)
            instance = new Focus();
        return instance;
    }
    public static final int CONTINUOUS_CODE = 2001;
    public static final int POMODORO_CODE = 2002;
    public static final int STOPWATCH_CODE = 2003;
    public static final int CODE_END = 2004;
    public static final int CODE_WORK = 2005;
    public static final int CODE_BREAK = 2005;


    public int POMODORO_CYCLE_COUNTER = 0;
    public void incrementPomodoroCounter() {
        POMODORO_CYCLE_COUNTER ++;
    }


    // encapsulated Focus state variable
    private boolean isRunning = false;
    public boolean getIsRunning() {
        return isRunning;
    }
    private void setIsRunning(boolean state) {
        isRunning = state;
    }


    // Alarm namespace or sth
    public AlarmManager alarmManager
            = (AlarmManager) MainActivity.getAppContext().getSystemService(Context.ALARM_SERVICE);
    PendingIntent piWork = null;
    PendingIntent piBreak = null;


    public void stopFocus() {
        if (piWork != null)
            alarmManager.cancel(piWork);
        if (piBreak != null)
            alarmManager.cancel(piBreak);

        POMODORO_CYCLE_COUNTER = 0;

        piBreak = piWork = null;

        setIsRunning(false);
        stopSelf();
    }


    /**
     * The Continuous mode starting function.
     * It setups an Alarm which handle ending of the mode.
     *
     * @param continuousDuration      duration of the focus mode
     * @param wakeDevice        boolean if user wants to wake device on start of every phase
     * @return                  returns boolean if starting pomodoro mode succeeded
     */
    public boolean startContinuous(long continuousDuration, boolean wakeDevice) {
        try {
            piWork = PendingIntent.getBroadcast(
                    MainActivity.getAppContext(),
                    CODE_END,
                    new Intent().setAction("com.oceanpeace.broadcasts.CONTINUOUS_END").setClass(MainActivity.getAppContext(), Broadcast.class),
                    PendingIntent.FLAG_CANCEL_CURRENT
            );


            if (wakeDevice) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        continuousDuration,
                        piWork
                );
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC,
                        continuousDuration,
                        piWork
                );
            }

            setIsRunning(true);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }


    /**
     * The Pomodoro mode starting function.
     * It setups two separate Alarms:
     * first for managing start of every work phase,
     * second for managing start of every break phase
     *
     * @param workDuration      duration in Millis of work phase
     * @param breakDuration     duration in Millis of break phase
     * @param wakeDevice        boolean if user wants to wake device on start of every phase
     * @return                  returns boolean if starting pomodoro mode succeeded
     */
    public boolean startPomodoro(long workDuration, long breakDuration, boolean wakeDevice) {
        try {
            piWork = PendingIntent.getBroadcast(
                    MainActivity.getAppContext(),
                    CODE_WORK,
                    new Intent().setAction("com.oceanpeace.broadcasts.POMODORO_START_WORK").setClass(MainActivity.getAppContext(), Broadcast.class),
                    PendingIntent.FLAG_CANCEL_CURRENT
            );
            piBreak = PendingIntent.getBroadcast(
                    MainActivity.getAppContext(),
                    CODE_BREAK,
                    new Intent().setAction("com.oceanpeace.broadcasts.POMODORO_START_BREAK").setClass(MainActivity.getAppContext(), Broadcast.class),
                    PendingIntent.FLAG_CANCEL_CURRENT
            );

            // setting up alarms
            if (wakeDevice) {
                // work phase
                alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP,
                        Calendar.getInstance().getTimeInMillis(),
                        workDuration + breakDuration,
                        piWork
                );
                // break phase
                alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP,
                        Calendar.getInstance().getTimeInMillis() + workDuration,
                        breakDuration,
                        piBreak
                );
            } else {
                // work phase
                alarmManager.setInexactRepeating(
                        AlarmManager.RTC,
                        Calendar.getInstance().getTimeInMillis(),
                        workDuration + breakDuration,
                        piWork
                );
                // break phase
                alarmManager.setInexactRepeating(
                        AlarmManager.RTC,
                        Calendar.getInstance().getTimeInMillis() + workDuration,
                        breakDuration,
                        piBreak
                );
            }

            setIsRunning(true);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }


    /**
     * The Stopwatch mode starting function.
     *
     * @return returns boolean if starting pomodoro mode succeeded
     */
    public boolean startStopwatch() {
        setIsRunning(true);
        return true;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}