package com.redinn.oceanpeace

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.util.Log
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.redinn.oceanpeace.FunctionBase.getFilesDir
import com.redinn.oceanpeace.managers.JSONManager.readFile
import com.redinn.oceanpeace.managers.JSONManager.writeFile
import org.json.JSONObject
import java.io.File

@CapacitorPlugin(name = "UI")
class UIPlugin : Plugin() {
    @PluginMethod
    fun fadeIn(call: PluginCall) {
        getBridge().webView.animate().alpha(0f).setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    call.resolve()
                }
            })
    }

    @PluginMethod
    fun fadeOut(call: PluginCall) {
        getBridge().webView.animate().alpha(1f).setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    call.resolve()
                }
            })
    }

    @PluginMethod
    fun setPreferences(call: PluginCall) {
        try {
            writeFile(
                call.getObject("data"), getFilesDir(
                    activity.applicationContext
                ) + "/settings.json"
            )
            call.resolve()
        } catch (e: Exception) {
            Log.e("UIPlugin", e.toString())
            call.reject(e.toString())
        }
    }

    @PluginMethod
    fun setPreference(call: PluginCall) {
        try {
            var data = JSONObject()
            try {
                data = readFile(
                    File(
                        getFilesDir(
                            activity.applicationContext
                        ) + "/settings.json"
                    )
                )
            } catch (e: Exception) {
                Log.e("UIPlugin", "settings.json does not exist $e")
            }
            data.put(call.getString("key"), call.getString("value"))
            writeFile(data, getFilesDir(activity.applicationContext) + "/settings.json")
            call.resolve()
        } catch (e: Exception) {
            Log.e("UIPlugin", e.toString())
            call.reject(e.toString())
        }
    }

    @PluginMethod
    fun getPreferences(call: PluginCall) {
        try {
            var data = JSONObject()
            try {
                data = readFile(
                    File(
                        getFilesDir(
                            activity.applicationContext
                        ) + "/settings.json"
                    )
                )
            } catch (e: Exception) {
                Log.e("UIPlugin", "settings.json does not exist $e")
            }
            val res = JSObject()
            res.put("data", data)
            call.resolve(res)
        } catch (e: Exception) {
            Log.e("UIPlugin", e.toString())
            call.reject(e.toString())
        }
    }
}
