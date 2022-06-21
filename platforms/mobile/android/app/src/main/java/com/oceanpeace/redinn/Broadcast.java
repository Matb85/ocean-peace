package com.oceanpeace.redinn;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Broadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction())
        {
            case AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED:

                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

    }
}