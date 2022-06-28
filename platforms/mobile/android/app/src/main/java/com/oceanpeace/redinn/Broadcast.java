package com.oceanpeace.redinn;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.widget.Toast;


import java.util.Calendar;

public class Broadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction())
        {
            case AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED:

                break;
                //focus time has come to an end case
            case "com.oceanpeace.broadcasts.CONTINUOUS_END":
                Toast.makeText(MainActivity.getAppContext(), "Focus has ended!", Toast.LENGTH_SHORT).show();
                break;
            case "com.oceanpeace.broadcasts.POMODORO_START_WORK":
                Focus.getInstance().incrementPomodoroCounter();
                Log.d("Pomodoro", Calendar.getInstance().getTime().toString() + " work started!");
                Toast.makeText(MainActivity.getAppContext(),"Work started!", Toast.LENGTH_SHORT).show();
                break;
            case "com.oceanpeace.broadcasts.POMODORO_START_BREAK":
                if (Focus.getInstance().POMODORO_CYCLE_COUNTER == 4)
                    Focus.getInstance().stopFocus();
                Log.d("Pomodoro", Calendar.getInstance().getTime().toString() + " break started!");
                Toast.makeText(MainActivity.getAppContext(),"Break started!", Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

    }
}