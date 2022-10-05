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

import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

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
    public void callMayo(PluginCall call) {
        if (getPermissionState("usage") != PermissionState.GRANTED && !hasPermission()) {

            requestPermissionForAlias("usage", call, "usagePermsCallback");
            startActivity(getActivity().getApplicationContext(), new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), Bundle.EMPTY);

        } else {
            runMayo(call);
        }
    }

    @PermissionCallback
    private void usagePermsCallback(PluginCall call) {
        if (getPermissionState("usage") == PermissionState.GRANTED && hasPermission()) {
            runMayo(call);
        } else {
            call.reject("Permission is required to run Mayo algorithm");
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



    void runMayo(PluginCall call) {
        Usage usage = new Usage(getActivity().getApplicationContext());
        JSObject ret = new JSObject();
        ret.put("stats", usage.GetUsageData());
        ret.put("total", usage.getTotalTime());
        call.resolve(ret);
    }




}
