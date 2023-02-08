package com.redinn.oceanpeace.usage;


import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

import java.util.Calendar;

@CapacitorPlugin(
        name="Usage",
        permissions = {
                @Permission(
                       alias = "usage",
                       strings = {
                                Manifest.permission.PACKAGE_USAGE_STATS
                        }
                )
        }
)
public class UsagePlugin extends Plugin {

    @PluginMethod()
    public void getAppsUsageToday(PluginCall call) {
        if (getPermissionState("usage") != PermissionState.GRANTED && !hasPermission()) {

            requestPermissionForAlias("usage", call, "usagePermsCallback_AppsUsed");
            startActivity(
                    getActivity().getApplicationContext(),
                    new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                    Bundle.EMPTY
            );

        } else {
            Usage usage = new Usage();
            JSObject ret = new JSObject();
            ret.put("stats", reduceStats(usage.getUsageData(getActivity().getApplicationContext())));

            call.resolve(ret);
        }
    }

    @PermissionCallback
    private void usagePermsCallback_AppsUsed(PluginCall call) {
        if (getPermissionState("usage") == PermissionState.GRANTED && hasPermission()) {
            Usage usage = new Usage();
            JSObject ret = new JSObject();
            ret.put("stats", reduceStats(usage.getUsageData(getActivity().getApplicationContext())));
            call.resolve(ret);
        } else {
            call.reject("Permission not granted");
        }
    }

    boolean hasPermission()
    {
        AppOpsManager opsManager = (AppOpsManager) getActivity().getApplicationContext().getSystemService(Context.APP_OPS_SERVICE);
        int mode = opsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(),getActivity().getApplicationContext().getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            return getActivity().getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED;
        }
        else {
            return mode == AppOpsManager.MODE_ALLOWED;
        }
    }


    JSArray reduceStats(JSArray stats) {
        JSArray ret = new JSArray();
        JSObject temp = new JSObject();

        try {

            for (int k = 0; k < 3; k++) {
                long max = 0;
                int idx = 0;
                // searching for max time spent
                for (int i = 0; i < stats.length(); i++) {
                    long a = stats.getJSONObject(i).getLong("minutes");
                    if (a > max) {
                        max = a;
                        idx = i;
                    }
                }
                ret.put(stats.getJSONObject(idx));
                stats.remove(idx);
            }
            long othersTime = 0;
            for (int i = 0; i < stats.length(); i++)
                othersTime += stats.getJSONObject(i).getLong("minutes");

            temp.put("minutes", othersTime);
            JSObject icon = new JSObject();
            icon.put("packageName", "");
            icon.put("label", "Other Apps");
            icon.put("iconPath", "");
            icon.put("version", "");
            temp.put("icon", icon);
            ret.put(temp);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("USAGE", "reduceStats: " + ret.toString());
        return  ret;
    }


    @PluginMethod()
    public void getTotalTime(PluginCall call) {
        if (getPermissionState("usage") != PermissionState.GRANTED && !hasPermission()) {

            requestPermissionForAlias("usage", call, "usagePermsCallback_Time");
            startActivity(
                    getActivity().getApplicationContext(),
                    new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                    Bundle.EMPTY
            );

        } else {
            Usage usage = new Usage();
            JSObject ret = new JSObject();
            ret.put("time", usage.getTotalTime(getActivity().getApplicationContext()));
            call.resolve(ret);
        }
    }

    @PermissionCallback
    private void usagePermsCallback_Time(PluginCall call) {
        if (getPermissionState("usage") == PermissionState.GRANTED && hasPermission()) {
            Usage usage = new Usage();
            JSObject ret = new JSObject();
            ret.put("time", usage.getTotalTime(getActivity().getApplicationContext()));
            call.resolve(ret);
        } else {
            call.reject("Permission not granted");
        }
    }



    @PluginMethod()
    public void getUnlocks(PluginCall call) {
        if (getPermissionState("usage") != PermissionState.GRANTED && !hasPermission()) {

            requestPermissionForAlias("usage", call, "usagePermsCallback_unlocks");
            startActivity(
                    getActivity().getApplicationContext(),
                    new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                    Bundle.EMPTY
            );

        } else {
            Log.i("TEST", "getUnlocks: starting!");
            Usage usage = new Usage();
            JSObject ret = new JSObject();
            ret.put("unlocks", usage.getUnlockStats(getActivity().getApplicationContext()));
            Log.i("TEST", "usagePermissionCallback_unlocks: " + ret.toString());
            call.resolve(ret);
        }
    }

    @PermissionCallback
    void usagePermissionCallback_unlocks(PluginCall call) {
        if (getPermissionState("usage") == PermissionState.GRANTED && hasPermission()) {

            Usage usage = new Usage();
            JSObject ret = new JSObject();
            ret.put("unlocks", usage.countUnlocks(Calendar.getInstance().getTimeInMillis()-10000,
                    Calendar.getInstance().getTimeInMillis(), getActivity().getApplicationContext()));
            Log.i("TEST", "usagePermissionCallback_unlocks: " + ret.toString());
            call.resolve(ret);
        } else {
            call.reject("Permission not granted");
        }
    }

}
