package com.redinn.oceanpeace.managers

import android.Manifest
import android.app.AppOpsManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import com.getcapacitor.JSObject
import com.getcapacitor.PermissionState
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.getcapacitor.annotation.Permission
import com.getcapacitor.annotation.PermissionCallback

@CapacitorPlugin(
    name = "Permissions",
    permissions = [Permission(
        alias = "usage",
        strings = [Manifest.permission.PACKAGE_USAGE_STATS]
    ), Permission(
        alias = "notificationPolicy",
        strings = [Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS]
    )]
)
class PermissionManager : Plugin() {
    var result: JSObject? = null
    @PluginMethod
    fun getAllPermissions(call: PluginCall) {
        result = JSObject()
        result!!.put("usage", false)
        result!!.put("notificationPolicy", false)
        if (getPermissionState("usage") != PermissionState.GRANTED && !hasUsagePermission()) {
            requestPermissionForAlias("usage", call, "usageCallback")
            ContextCompat.startActivity(
                activity.applicationContext,
                Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                Bundle.EMPTY
            )
        }
        if (getPermissionState("notificationPolicy") != PermissionState.GRANTED && !hasNotificationPolicyPermission()) {
            requestPermissionForAlias("notificationPolicy", call, "notificationPolicyCallback")
            ContextCompat.startActivity(
                activity.applicationContext,
                Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                Bundle.EMPTY
            )
        }
        call.resolve(result)
    }

    @PermissionCallback
    private fun usageCallback(call: PluginCall) {
        result!!.put("usage", getPermissionState("usage") == PermissionState.GRANTED)
    }

    @PermissionCallback
    private fun notificationPolicyCallback(call: PluginCall) {
        result!!.put(
            "notificationPolicy",
            getPermissionState("notificationPolicy") == PermissionState.GRANTED
        )
    }

    fun hasUsagePermission(): Boolean {
        val opsManager =
            activity.applicationContext.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = opsManager.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            activity.applicationContext.packageName
        )
        return if (mode == AppOpsManager.MODE_DEFAULT) {
            activity.applicationContext.checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
        } else {
            mode == AppOpsManager.MODE_ALLOWED
        }
    }

    fun hasNotificationPolicyPermission(): Boolean {
        val temp: Boolean
        temp = activity.applicationContext.getSystemService(
            NotificationManager::class.java
        ).isNotificationPolicyAccessGranted
        Log.i("PermissionManager", "hasNotificationPolicyPermission: $temp")
        return temp
    }
}
