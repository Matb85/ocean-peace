package com.oceanpeace.redinn;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.widget.Toast;


public class Broadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction())
        {
            case AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED:

                break;
                //focus time has come to an end case
            case "com.oceanpeace.broadcasts.FOCUS_END":
                Toast.makeText(MainActivity.getAppContext(), "Focus has ended!", Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

    }
}