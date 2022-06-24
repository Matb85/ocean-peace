package com.oceanpeace.redinn.focus;

import android.os.Build;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(
        name="Focus"
)
public class FocusPlugin extends Plugin {



    @PluginMethod
    public void startContinuous(PluginCall call) {
        JSObject ret = new JSObject();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if(!Focus.getInstance().alarmManager.canScheduleExactAlarms()) {

            }
        }
        else
        {
            boolean started = Focus.getInstance().startContinues(1000*10);
            ret.put("started", started);
        }
        call.resolve(ret);
    }
}
