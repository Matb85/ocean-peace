package com.redinn.oceanpeace.mayo.keepalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class KeepAliveReceiver extends BroadcastReceiver {
    private static final String TAG = "KeepAliveReceiver";
    public static final String RESTART_API_SERVICE = "mayo.restart.api";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "broadcast received");

        WorkManager workManager = WorkManager.getInstance(context);
        OneTimeWorkRequest startService = new OneTimeWorkRequest.Builder(KeepAliveWorker.class).build();

        workManager.enqueue(startService);
    }
}
