package com.redinn.oceanpeace;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.work.WorkManager;

import com.getcapacitor.BridgeActivity;
import com.redinn.oceanpeace.focus.FocusPlugin;
import com.redinn.oceanpeace.focus.FocusService;
import com.redinn.oceanpeace.goals.GoalsPlugin;
import com.redinn.oceanpeace.icons.IconManager;
import com.redinn.oceanpeace.icons.IconsPlugin;
import com.redinn.oceanpeace.managers.PermissionManager;
import com.redinn.oceanpeace.mayo.MayoAPI;
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

        bindAPI();

        IconManager.regenerateIcons(getApplicationContext());

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
        WorkManager.getInstance(getApplicationContext()).cancelAllWork();
        unbindAPI();
        super.onDestroy();
    }



    // region Mayo API
    public static MayoAPI mAPI;
    public static boolean mBound= false;

    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            MayoAPI.LocalBinder binder = (MayoAPI.LocalBinder) service;
            mAPI = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    void bindAPI() {
        if (mBound)
            return;

        Intent intent = new Intent(this, MayoAPI.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);

    }

    void unbindAPI() {
        if (!mBound)
            return;

        unbindService(mConnection);
    }
    // endregion Mayo API

    //region focus

    public static void connectFocus(Context context) {
        Intent create = new Intent().setClass(context, FocusService.class);

        context.startService(create);


        Intent bind = new Intent(context, FocusService.class);

        context.startService(bind);
        context.bindService(bind, focusConnection, Context.BIND_AUTO_CREATE);


        //poor await
        while(focusService == null) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Log.i("T", "connectFocus: " + focusService.toString());
    }

    public static FocusService focusService;
    public static boolean focusBound = false;

    private static final ServiceConnection focusConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            FocusService.LocalBinder binder = (FocusService.LocalBinder) service;
            focusService = binder.getService();
            focusBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    //endregion Focus
}
