package com.oceanpeace.redinn.focus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

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
    public static final int FOCUS_CODE_START = 2001;
    public static final int FOCUS_CODE_END = 2002;

    // Alarm namespace or sth
    public AlarmManager alarmManager
            = (AlarmManager) MainActivity.getAppContext().getSystemService(Context.ALARM_SERVICE);
    public boolean startContinues(long durationInMillis) {
        try {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC,
                    Calendar.getInstance().getTimeInMillis() + durationInMillis,
                    PendingIntent.getBroadcast(
                            MainActivity.getAppContext(),
                            FOCUS_CODE_END,
                            new Intent().setAction("com.oceanpeace.broadcasts.FOCUS_END").setClass(MainActivity.getAppContext(), Broadcast.class),
                            PendingIntent.FLAG_CANCEL_CURRENT
                    )
            );
            setIsRunning(true);
            Toast.makeText(MainActivity.getAppContext(), "WEWEWEW", Toast.LENGTH_SHORT).show();
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    public void stopFocus(){
        setIsRunning(false);
        stopSelf();
    }

    // encapsulated Focus state variable
    private boolean isRunning = false;
    public boolean getIsRunning() {
        return isRunning;
    }
    private void setIsRunning(boolean state) {
        isRunning = state;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}

