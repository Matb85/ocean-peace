package com.redinn.oceanpeace.managers;


import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

@CapacitorPlugin(
        name = "Permissions",
        permissions = {
                @Permission(
                        alias = "usage",
                        strings = {
                                Manifest.permission.PACKAGE_USAGE_STATS
                        }
                ),
                @Permission(
                        alias = "notificationPolicy",
                        strings = {
                                Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
                        }
                )
        }
)
public class PermissionManager extends Plugin {

    JSObject result;

    @PluginMethod
    public void getAllPermissions(PluginCall call) {
        result = new JSObject();
        result.put("usage", false);
        result.put("notificationPolicy", false);

        if (getPermissionState("usage") != PermissionState.GRANTED && !hasUsagePermission()) {
            requestPermissionForAlias("usage", call, "usageCallback");
            startActivity(getActivity().getApplicationContext(), new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), Bundle.EMPTY);
        }
        if (getPermissionState("notificationPolicy") != PermissionState.GRANTED && !hasNotificationPolicyPermission()) {
            requestPermissionForAlias("notificationPolicy", call, "notificationPolicyCallback");
            startActivity(getActivity().getApplicationContext(), new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), Bundle.EMPTY);
        }

        call.resolve(result);
    }

    @PermissionCallback
    private void usageCallback(PluginCall call) {
        result.put("usage", getPermissionState("usage") == PermissionState.GRANTED);
    }

    @PermissionCallback
    private void notificationPolicyCallback(PluginCall call) {
        result.put("notificationPolicy", getPermissionState("notificationPolicy") == PermissionState.GRANTED);
    }

    boolean hasUsagePermission() {
        AppOpsManager opsManager = (AppOpsManager) getActivity().getApplicationContext().getSystemService(Context.APP_OPS_SERVICE);
        int mode = opsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), getActivity().getApplicationContext().getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            return getActivity().getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED;
        } else {
            return mode == AppOpsManager.MODE_ALLOWED;
        }
    }

    boolean hasNotificationPolicyPermission() {
        boolean temp;
        temp = getActivity().getApplicationContext().getSystemService(NotificationManager.class).isNotificationPolicyAccessGranted();
        Log.i("PermissionManager", "hasNotificationPolicyPermission: " + temp);
        return temp;
    }
}
