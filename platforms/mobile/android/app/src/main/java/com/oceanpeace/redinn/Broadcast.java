package com.oceanpeace.redinn;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.oceanpeace.redinn.focus.Focus;

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
                Toast.makeText(context, "Focus has ended!", Toast.LENGTH_SHORT).show();
                break;
            case "com.oceanpeace.broadcasts.POMODORO_START_WORK":
                Focus.incrementPomodoroCounter();
                Log.d("Pomodoro", Calendar.getInstance().getTime().toString() + " work started!");
                Toast.makeText(context,"Work started!", Toast.LENGTH_SHORT).show();
                break;
            case "com.oceanpeace.broadcasts.POMODORO_START_BREAK":
                if (Focus.POMODORO_CYCLE_COUNTER == 4)
                    Focus.getInstance().stopFocus();
                Log.d("Pomodoro", Calendar.getInstance().getTime().toString() + " break started!");
                Toast.makeText(context,"Break started!", Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

    }
}