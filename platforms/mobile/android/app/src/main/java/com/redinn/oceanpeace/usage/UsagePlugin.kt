package com.redinn.oceanpeace.usage

import android.Manifest
import android.util.Log
import com.getcapacitor.JSObject
import com.getcapacitor.PermissionState
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.getcapacitor.annotation.Permission
import com.getcapacitor.annotation.PermissionCallback
import com.redinn.oceanpeace.helper.PermissionHelper.hasAppUsagePermission


@CapacitorPlugin(
    name = "Usage",
    permissions = [Permission(alias = "usage", strings = [Manifest.permission.PACKAGE_USAGE_STATS])]
)
class UsagePlugin : Plugin() {
    val TAG = "USAGE_PLUGIN"

    @PluginMethod
    fun getAppsUsageToday(call: PluginCall) {
        if (!permissionGranted(call, "getAppsUsageTodayCallback"))
            return
        returnPrimitiveData(call, Usage.getUsageData(activity.applicationContext))
    }

    @PermissionCallback
    private fun getAppsUsageTodayCallback(call: PluginCall) {
        if (getPermissionState("usage") != PermissionState.GRANTED) {
            call.reject("Permission is required")
            return
        }
        returnPrimitiveData(call, Usage.getUsageData(activity.applicationContext))
    }

    @PluginMethod
    fun getTotalTime(call: PluginCall) {
        if (!permissionGranted(call, "getTotalTimeCallback"))
            return
        returnPrimitiveData(call, Usage.getTotalTime(activity.applicationContext))
    }

    @PermissionCallback
    private fun getTotalTimeCallback(call: PluginCall) {
        if (getPermissionState("usage") != PermissionState.GRANTED) {
            call.reject("Permission is required")
            return
        }
        returnPrimitiveData(call, Usage.getTotalTime(activity.applicationContext))
    }

    @PluginMethod
    fun getUnlocks(call: PluginCall) {
        if (!permissionGranted(call, "getUnlocksCallback"))
            return
        returnPrimitiveData(call, Usage.getUnlockStats(activity.applicationContext))
    }

    @PermissionCallback
    private fun getUnlocksCallback(call: PluginCall) {
        if (getPermissionState("usage") != PermissionState.GRANTED) {
            call.reject("Permission is required")
            return
        }
        returnPrimitiveData(call, Usage.getUnlockStats(activity.applicationContext))
    }

    private fun permissionGranted(call: PluginCall, callback: String): Boolean {
        Log.d(TAG, "Method with callback $callback triggered")
        if (getPermissionState("usage") != PermissionState.GRANTED && !hasAppUsagePermission(
                activity.applicationContext
            )
        ) {
            requestPermissionForAlias("usage", call, callback)
            return false
        }
        return true
    }

    private fun returnPrimitiveData(call: PluginCall, data: Any) {
        val ret = JSObject()
        ret.put("data", data)
        Log.d(TAG, data.toString())
        Log.d(TAG, "Method successfully ended")
        call.resolve(ret)
    }
}
