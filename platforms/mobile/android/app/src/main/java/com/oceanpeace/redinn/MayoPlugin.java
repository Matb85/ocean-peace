package com.oceanpeace.redinn;



import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

@CapacitorPlugin(
        name="Mayo",
        permissions = {
                @Permission(
                       alias = "usage",
                       strings = {
                                Manifest.permission.PACKAGE_USAGE_STATS
                        }
                )
        }
)
public class MayoPlugin extends Plugin {

    @PluginMethod()
    public void callMayo(PluginCall call) {
        if (getPermissionState("usage") != PermissionState.GRANTED && !hasPermission()) {

            requestPermissionForAlias("usage", call, "usagePermsCallback");
            startActivity(MainActivity.getAppContext(), new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), Bundle.EMPTY);

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

    void runMayo(PluginCall call) {
        Mayo mayo = new Mayo();
        JSObject ret = new JSObject();
        ret.put("stats", mayo.GetUsageData());
        call.resolve(ret);
    }

    boolean hasPermission()
    {
        AppOpsManager opsManager = (AppOpsManager) MainActivity.getAppContext().getSystemService(Context.APP_OPS_SERVICE);
        int mode = opsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(),MainActivity.getAppContext().getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
             return MainActivity.getAppContext().checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED;
        }
        else {
            return mode == AppOpsManager.MODE_ALLOWED;
        }
    }
}
