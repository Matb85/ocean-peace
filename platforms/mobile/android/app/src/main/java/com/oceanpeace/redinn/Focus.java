package com.oceanpeace.redinn;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class Focus extends Service {
    private static Focus instance;

    public static Focus getInstance()
    {
        if (instance == null)
            instance = new Focus();
        return instance;
    }


    // Alarm namespace or sth
    public AlarmManager alarmManager
            = (AlarmManager) MainActivity.getAppContext().getSystemService(Context.ALARM_SERVICE);
//    public void setFocus (long durationInMillis) {
//        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC,
//                Calendar.getInstance().getTimeInMillis() + durationInMillis,
//                ///Pendingintent
//        );
//    }



    // encapsulated Focus state variable
    private boolean isRunning = false;
    public boolean getIsRunning() {
        return isRunning;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

