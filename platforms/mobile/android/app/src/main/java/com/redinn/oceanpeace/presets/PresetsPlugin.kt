package com.redinn.oceanpeace.presets

import android.content.Context
import android.util.Log
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.redinn.oceanpeace.FunctionBase
import com.redinn.oceanpeace.managers.JSONManager
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

@CapacitorPlugin(name = "Presets")
class PresetsPlugin : Plugin() {
    private fun getPresetsFolder(ctx: Context): String {
        return FunctionBase.getFilesDir(ctx) + "/presets"
    }

    override fun load() {
        val ctx = activity.applicationContext
        val iconFolderFile = File(getPresetsFolder(ctx))
        if (!iconFolderFile.isDirectory) {
            Log.d("PresetsPlugin", "creating the presets folder")
            iconFolderFile.mkdir()
        }
    }

    @PluginMethod
    fun savePreset(call: PluginCall) {
        /* get data */
        val ctx = activity.applicationContext
        val data: JSONObject = call.getObject("data")
        try {
            val fileName = getPresetsFolder(ctx) + "/" + data.getString("id") + ".json"
            Log.d("PresetsPlugin", fileName + " " + data.toString(0))
            JSONManager.writeFile(data, fileName)
        } catch (err: Exception) {
            Log.e("PresetsPlugin", err.toString())
        }
        call.resolve()
    }

    @PluginMethod
    fun getPreset(call: PluginCall) {
        val ctx = activity.applicationContext
        val res = JSObject()
        val file = File(getPresetsFolder(ctx) + "/" + call.getString("id") + ".json")
        try {
            res.put("preset", JSONManager.readFile(file))
        } catch (err: Exception) {
            Log.e("PresetsPlugin", err.toString())
        }
        call.resolve(res)
    }

    @PluginMethod
    fun getAllPresets(call: PluginCall) {
        val ctx = activity.applicationContext
        val res = JSObject()
        val files = File(getPresetsFolder(ctx)).listFiles()
        /* put data from each goal the the final array */
        val arr = JSONArray()
        if (files == null) {
            Log.d("PresetsPlugin", "no presets, aborting")
            res.put("presets", arr)
            call.resolve(res)
            return
        }
        Log.i("PresetsPlugin", "iterating through all files...")
        for (file in files) {
            Log.i("PresetsPlugin", "reading file " + file.name)
            try {
                arr.put(JSONManager.readFile(file))
            } catch (err: Exception) {
                Log.e("PresetsPlugin", err.toString())
            }
        }
        res.put("presets", arr)
        call.resolve(res)
    }

    @PluginMethod
    fun deletePreset(call: PluginCall) {
        val ctx = activity.applicationContext
        val filePath = getPresetsFolder(ctx) + "/" + call.getString("id") + ".json"
        val file = File(filePath)
        Log.d("PresetsPlugin", "deleting file $filePath")
        file.delete()
        call.resolve()
    }
}
