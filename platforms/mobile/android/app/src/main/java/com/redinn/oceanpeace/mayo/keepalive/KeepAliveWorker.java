package com.redinn.oceanpeace.mayo.keepalive;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.redinn.oceanpeace.mayo.MayoAPI;

public class KeepAliveWorker extends Worker {
    private final Context workerContext;
    private static final String TAG = "KeepAliveWorker";


    public KeepAliveWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.workerContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork called for: " + this.getId());
        Log.d(TAG, "Service Running: " + MayoAPI.isServiceRunning);

        if (!MayoAPI.isServiceRunning) {
            Log.d(TAG, "starting service from doWork...");
            Intent intent = new Intent(this.workerContext, MayoAPI.class);
            ContextCompat.startForegroundService(workerContext, intent);
        }

        return Result.success();
    }


    @Override
    public void onStopped() {
        Log.d(TAG, "onStopped called for: " + this.getId());
        super.onStopped();
    }
}
