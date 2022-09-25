package com.oceanpeace.redinn;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.os.IBinder;

import androidx.work.WorkManager;

import com.getcapacitor.BridgeActivity;
import com.oceanpeace.redinn.focus.Focus;
import com.oceanpeace.redinn.focus.FocusPlugin;
import com.oceanpeace.redinn.goals.GoalsPlugin;
import com.oceanpeace.redinn.icons.IconManager;
import com.oceanpeace.redinn.icons.IconsPlugin;
import com.oceanpeace.redinn.mayo.MayoAPI;
import com.oceanpeace.redinn.presets.PresetsPlugin;
import com.oceanpeace.redinn.schedule.SchedulePlugin;
import com.oceanpeace.redinn.usage.UsagePlugin;

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

        //endregion

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        //WorkManager.getInstance(getApplicationContext()).enqueue(IconWorker.regenerateIcons);

        bindAPI();

        IconManager.regenerateIcons(getApplicationContext());

        Focus.getInstance().setContextElements(this.getApplicationContext());

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



    // region API
    public static MayoAPI mAPI;
    public static boolean mBound= false;

    private ServiceConnection mConnection = new ServiceConnection() {

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
    // endregion

}
