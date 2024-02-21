package com.redinn.oceanpeace.usage

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import com.getcapacitor.JSArray
import com.getcapacitor.JSObject
import com.getcapacitor.PermissionState
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.getcapacitor.annotation.Permission
import com.getcapacitor.annotation.PermissionCallback
import java.util.Calendar

@CapacitorPlugin(
    name = "Usage",
    permissions = [Permission(alias = "usage", strings = [Manifest.permission.PACKAGE_USAGE_STATS])]
)
class UsagePlugin : Plugin() {
    @PluginMethod
    fun getAppsUsageToday(call: PluginCall) {
        if (getPermissionState("usage") != PermissionState.GRANTED && !hasPermission()) {
            requestPermissionForAlias("usage", call, "usagePermsCallback_AppsUsed")
            ContextCompat.startActivity(
                activity.applicationContext,
                Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                Bundle.EMPTY
            )
        } else {
            val usage = Usage()
            val ret = JSObject()
            ret.put("stats", reduceStats(usage.getUsageData(activity.applicationContext)))
            call.resolve(ret)
        }
    }

    @PermissionCallback
    private fun usagePermsCallback_AppsUsed(call: PluginCall) {
        if (getPermissionState("usage") == PermissionState.GRANTED && hasPermission()) {
            val usage = Usage()
            val ret = JSObject()
            ret.put("stats", reduceStats(usage.getUsageData(activity.applicationContext)))
            call.resolve(ret)
        } else {
            call.reject("Permission not granted")
        }
    }

    fun hasPermission(): Boolean {
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

    fun reduceStats(stats: JSArray): JSArray {
        val ret = JSArray()
        val temp = JSObject()
        try {
            for (k in 0..2) {
                var max: Long = 0
                var idx = 0
                // searching for max time spent
                for (i in 0 until stats.length()) {
                    val a = stats.getJSONObject(i).getLong("minutes")
                    if (a > max) {
                        max = a
                        idx = i
                    }
                }
                ret.put(stats.getJSONObject(idx))
                stats.remove(idx)
            }
            var othersTime: Long = 0
            for (i in 0 until stats.length()) othersTime += stats.getJSONObject(i)
                .getLong("minutes")
            temp.put("minutes", othersTime)
            val icon = JSObject()
            icon.put("packageName", "")
            icon.put("label", "Other Apps")
            icon.put("iconPath", "")
            icon.put("version", "")
            temp.put("icon", icon)
            ret.put(temp)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.i("USAGE", "reduceStats: $ret")
        return ret
    }

    @PluginMethod
    fun getTotalTime(call: PluginCall) {
        if (getPermissionState("usage") != PermissionState.GRANTED && !hasPermission()) {
            requestPermissionForAlias("usage", call, "usagePermsCallback_Time")
            ContextCompat.startActivity(
                activity.applicationContext,
                Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                Bundle.EMPTY
            )
        } else {
            val usage = Usage()
            val ret = JSObject()
            ret.put("time", usage.getTotalTime(activity.applicationContext))
            call.resolve(ret)
        }
    }

    @PermissionCallback
    private fun usagePermsCallback_Time(call: PluginCall) {
        if (getPermissionState("usage") == PermissionState.GRANTED && hasPermission()) {
            val usage = Usage()
            val ret = JSObject()
            ret.put("time", usage.getTotalTime(activity.applicationContext))
            call.resolve(ret)
        } else {
            call.reject("Permission not granted")
        }
    }

    @PluginMethod
    fun getUnlocks(call: PluginCall) {
        if (getPermissionState("usage") != PermissionState.GRANTED && !hasPermission()) {
            requestPermissionForAlias("usage", call, "usagePermsCallback_unlocks")
            ContextCompat.startActivity(
                activity.applicationContext,
                Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                Bundle.EMPTY
            )
        } else {
            Log.i("TEST", "getUnlocks: starting!")
            val usage = Usage()
            val ret = JSObject()
            ret.put("unlocks", usage.getUnlockStats(activity.applicationContext))
            Log.i("TEST", "usagePermissionCallback_unlocks: $ret")
            call.resolve(ret)
        }
    }

    @PermissionCallback
    fun usagePermissionCallback_unlocks(call: PluginCall) {
        if (getPermissionState("usage") == PermissionState.GRANTED && hasPermission()) {
            val usage = Usage()
            val ret = JSObject()
            ret.put(
                "unlocks", usage.countUnlocks(
                    Calendar.getInstance().timeInMillis - 10000,
                    Calendar.getInstance().timeInMillis, activity.applicationContext
                )
            )
            Log.i("TEST", "usagePermissionCallback_unlocks: $ret")
            call.resolve(ret)
        } else {
            call.reject("Permission not granted")
        }
    }
}
