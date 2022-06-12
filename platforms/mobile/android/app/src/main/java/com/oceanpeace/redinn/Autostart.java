package com.oceanpeace.redinn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class Autostart extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg) {

        Intent intent = new Intent(context, Mayo.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
        Log.i("Autostart", "started");

        throw new UnsupportedOperationException("Not yet implemented");
    }
}