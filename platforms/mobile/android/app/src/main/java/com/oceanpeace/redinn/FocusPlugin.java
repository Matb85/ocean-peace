package com.oceanpeace.redinn;

import android.Manifest;
import android.app.AlarmManager;
import android.os.Build;
import android.provider.Settings;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;

@CapacitorPlugin(
        name="Focus"
)
public class FocusPlugin extends Plugin {



    @PluginMethod
    public void callFocus(PluginCall call) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if(!Focus.getInstance().alarmManager.canScheduleExactAlarms()) {

            }
        }
        else
        {

        }
    }
}
