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
        boolean wake = Boolean.FALSE.equals(call.getBoolean("wakeDevice"));
        long duration = 0;
        if (call.getLong("continuousDuration") != null)
            duration = call.getLong("continuousDuration");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if(!Focus.getInstance().alarmManager.canScheduleExactAlarms()) {

            }
        }
        else
        {
            boolean started = Focus.getInstance().startContinuous(duration, wake, getActivity().getApplicationContext());
            ret.put("started", started);
        }
        call.resolve(ret);
    }

    @PluginMethod
    public void startPomodoro(PluginCall call) {
        JSObject ret = new JSObject();
        boolean wake = Boolean.FALSE.equals(call.getBoolean("wakeDevice"));
        long workDuration = 0;
        if (call.getLong("continuousDuration") != null)
            workDuration = call.getLong("continuousDuration");
        long breakDuration = 0;
        if (call.getLong("continuousDuration") != null)
            breakDuration = call.getLong("continuousDuration");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if(!Focus.getInstance().alarmManager.canScheduleExactAlarms()) {

            }
        }
        else
        {
            boolean started = Focus.getInstance().startPomodoro(workDuration, breakDuration, wake, getActivity().getApplicationContext());
            ret.put("started", started);
        }
        call.resolve(ret);
    }

    @PluginMethod
    public void startStopwatch(PluginCall call) {
        JSObject ret = new JSObject();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if(!Focus.getInstance().alarmManager.canScheduleExactAlarms()) {

            }
        }
        else
        {
            boolean started = Focus.getInstance().startStopwatch();
            ret.put("started", started);
        }
        call.resolve(ret);
    }

    @PluginMethod
    public void stopFocus(PluginCall call) {
        Focus.getInstance().stopFocus();
        call.resolve();
    }
}
