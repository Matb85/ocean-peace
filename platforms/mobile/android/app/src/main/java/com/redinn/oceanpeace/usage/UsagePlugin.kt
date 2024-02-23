package com.redinn.oceanpeace.usage

import android.Manifest
import android.content.Intent
import android.os.Bundle
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
import com.redinn.oceanpeace.helper.PermissionHelper.hasAppUsagePermission
import java.util.Calendar

@CapacitorPlugin(
    name = "Usage",
    permissions = [Permission(alias = "usage", strings = [Manifest.permission.PACKAGE_USAGE_STATS])]
)
class UsagePlugin : Plugin() {
    @PluginMethod
    fun getAppsUsageToday(call: PluginCall) {
        if (getPermissionState("usage") != PermissionState.GRANTED && !hasAppUsagePermission(
                activity.applicationContext
            )
        ) {
            requestPermissionForAlias("usage", call, "usagePermsCallback_AppsUsed")
            ContextCompat.startActivity(
                activity.applicationContext,
                Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                Bundle.EMPTY
            )
        } else {
            val ret = JSObject()
            ret.put("stats", reduceStats(Usage.getUsageData(activity.applicationContext)))
            call.resolve(ret)
        }
    }

    @PermissionCallback
    private fun usagePermsCallback_AppsUsed(call: PluginCall) {
        if (getPermissionState("usage") == PermissionState.GRANTED && hasAppUsagePermission(activity.applicationContext)) {
            val ret = JSObject()
            ret.put("stats", reduceStats(Usage.getUsageData(activity.applicationContext)))
            call.resolve(ret)
        } else {
            call.reject("Permission not granted")
        }
    }

    private fun reduceStats(stats: JSArray): JSArray {
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
        if (getPermissionState("usage") != PermissionState.GRANTED && !hasAppUsagePermission(
                activity.applicationContext
            )
        ) {
            requestPermissionForAlias("usage", call, "usagePermsCallback_Time")
            ContextCompat.startActivity(
                activity.applicationContext,
                Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                Bundle.EMPTY
            )
        } else {
            val res = JSObject()
            res.put("time", Usage.getTotalTime(activity.applicationContext))
            call.resolve(res)
        }
    }

    @PermissionCallback
    fun usagePermsCallback_Time(call: PluginCall) {
        if (getPermissionState("usage") == PermissionState.GRANTED && hasAppUsagePermission(activity.applicationContext)) {
            val ret = JSObject()
            ret.put("time", Usage.getTotalTime2(activity.applicationContext) / 1000)
            call.resolve(ret)
        } else {
            call.reject("Permission not granted")
        }
    }

    @PluginMethod
    fun getUnlocks(call: PluginCall) {
        if (getPermissionState("usage") != PermissionState.GRANTED && !hasAppUsagePermission(
                activity.applicationContext
            )
        ) {
            requestPermissionForAlias("usage", call, "usagePermsCallback_unlocks")
            ContextCompat.startActivity(
                activity.applicationContext,
                Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                Bundle.EMPTY
            )
        } else {
            Log.i("TEST", "getUnlocks: starting!")
            val ret = JSObject()
            ret.put("unlocks", Usage.getUnlockStats(activity.applicationContext))
            Log.i("TEST", "usagePermissionCallback_unlocks: $ret")
            call.resolve(ret)
        }
    }

    @PermissionCallback
    fun usagePermissionCallback_unlocks(call: PluginCall) {
        if (getPermissionState("usage") == PermissionState.GRANTED && hasAppUsagePermission(activity.applicationContext)) {
            val ret = JSObject()
            ret.put(
                "unlocks", Usage.countUnlocks(
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
