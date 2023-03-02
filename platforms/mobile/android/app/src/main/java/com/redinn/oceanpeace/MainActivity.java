package com.redinn.oceanpeace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.getcapacitor.BridgeActivity;
import com.redinn.oceanpeace.focus.FocusPlugin;
import com.redinn.oceanpeace.goals.GoalsPlugin;
import com.redinn.oceanpeace.icons.IconManager;
import com.redinn.oceanpeace.icons.IconsPlugin;
import com.redinn.oceanpeace.managers.PermissionManager;
import com.redinn.oceanpeace.mayo.MayoAPI;
import com.redinn.oceanpeace.mayo.keepalive.KeepAliveWorker;
import com.redinn.oceanpeace.presets.PresetsPlugin;
import com.redinn.oceanpeace.schedule.SchedulePlugin;
import com.redinn.oceanpeace.usage.UsagePlugin;

import java.util.concurrent.TimeUnit;

public class MainActivity extends BridgeActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {


        //region Plugin Registration

        registerPlugin(UsagePlugin.class);
        registerPlugin(FocusPlugin.class);
        registerPlugin(GoalsPlugin.class);
        registerPlugin(IconsPlugin.class);
        registerPlugin(PresetsPlugin.class);
        registerPlugin(SchedulePlugin.class);
        registerPlugin(UIPlugin.class);
        registerPlugin(PermissionManager.class);

        //endregion


        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                IconManager.regenerateIcons(getApplicationContext());
            }
        }).start();

        startServiceViaWorker();
        startService();

        super.onStart();

        // TODO: make checking for usageStats permissions & accessibility
        // TODO: make notifications and automatic permissions setting
    }

    @Override
    public void onStop() {



        super.onStop();
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, MayoAPI.class));
        super.onDestroy();
    }


    public void startService() {
        Log.d("Main", "startService called");
        if (!MayoAPI.isServiceRunning) {
            Intent serviceIntent = new Intent(this, MayoAPI.class);
            ContextCompat.startForegroundService(this, serviceIntent);
        }
    }

    public void startServiceViaWorker() {
        Log.d("Main", "startServiceViaWorker called");
        String UNIQUE_WORK_NAME = "StartMyServiceViaWorker";
        WorkManager workManager = WorkManager.getInstance(this);

        // As per Documentation: The minimum repeat interval that can be defined is 15 minutes
        // (same as the JobScheduler API), but in practice 15 doesn't work. Using 16 here
        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(
                        KeepAliveWorker.class,
                        16,
                        TimeUnit.MINUTES)
                        .build();

        // to schedule a unique work, no matter how many times app is opened i.e. startServiceViaWorker gets called
        // do check for AutoStart permission
        workManager.enqueueUniquePeriodicWork(UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request);

    }
}
