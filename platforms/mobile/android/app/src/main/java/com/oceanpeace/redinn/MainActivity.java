package com.oceanpeace.redinn;

import android.os.Bundle;

import androidx.work.WorkManager;

import com.getcapacitor.BridgeActivity;
import com.oceanpeace.redinn.focus.Focus;
import com.oceanpeace.redinn.focus.FocusPlugin;
import com.oceanpeace.redinn.goals.GoalsPlugin;
import com.oceanpeace.redinn.icons.IconManager;
import com.oceanpeace.redinn.icons.IconsPlugin;
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

        //endregion

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {

        //WorkManager.getInstance(getApplicationContext()).enqueue(IconWorker.regenerateIcons);

        IconManager.regenerateIcons(getApplicationContext());

        Focus.getInstance().setContextElements(this.getApplicationContext());

        super.onStart();

        // TODO: make checking for usageStats permissions & accessibility
        // TODO: make notifications and automatic permissions setting
    }

    @Override
    public void onDestroy() {
        WorkManager.getInstance(getApplicationContext()).cancelAllWork();
        super.onDestroy();
    }
}
