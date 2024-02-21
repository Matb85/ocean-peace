package com.redinn.oceanpeace.schedule

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

@CapacitorPlugin(name = "Schedule")
class SchedulePlugin : Plugin() {
    private fun getSchedulesFolder(ctx: Context): String {
        return FunctionBase.getFilesDir(ctx) + "/schedule"
    }

    override fun load() {
        val ctx = activity.applicationContext
        val iconFolderFile = File(getSchedulesFolder(ctx))
        if (!iconFolderFile.isDirectory) {
            Log.d("SchedulesPlugin", "creating the schedule folder")
            iconFolderFile.mkdir()
        }
    }

    @PluginMethod
    fun saveSchedule(call: PluginCall) {
        /* get data */
        val ctx = activity.applicationContext
        val data: JSONObject = call.getObject("data")
        try {
            val fileName = getSchedulesFolder(ctx) + "/" + data.getString("id") + ".json"
            Log.d("SchedulesPlugin", fileName + " " + data.toString(0))
            JSONManager.writeFile(data, fileName)
        } catch (err: Exception) {
            Log.e("SchedulesPlugin", err.toString())
        }
        call.resolve()
    }

    @PluginMethod
    fun getSchedule(call: PluginCall) {
        val ctx = activity.applicationContext
        val res = JSObject()
        val file = File(getSchedulesFolder(ctx) + "/" + call.getString("id") + ".json")
        try {
            res.put("schedule", JSONManager.readFile(file))
        } catch (err: Exception) {
            Log.e("SchedulesPlugin", err.toString())
        }
        call.resolve(res)
    }

    @PluginMethod
    fun getAllSchedules(call: PluginCall) {
        val ctx = activity.applicationContext
        val res = JSObject()
        val files = File(getSchedulesFolder(ctx)).listFiles()
        /* put data from each goal the the final array */
        val arr = JSONArray()
        if (files == null) {
            Log.d("SchedulesPlugin", "no Schedules, aborting")
            res.put("schedules", arr)
            call.resolve(res)
            return
        }
        Log.d("SchedulesPlugin", "iterating through all files...")
        for (file in files) {
            Log.d("SchedulesPlugin", "reading file " + file.name)
            try {
                arr.put(JSONManager.readFile(file))
            } catch (err: Exception) {
                Log.e("SchedulesPlugin", err.toString())
            }
        }
        res.put("schedules", arr)
        call.resolve(res)
    }

    @PluginMethod
    fun deleteSchedule(call: PluginCall) {
        val ctx = activity.applicationContext
        val filePath = getSchedulesFolder(ctx) + "/" + call.getString("id") + ".json"
        val file = File(filePath)
        Log.d("SchedulesPlugin", "deleting file $filePath")
        file.delete()
        call.resolve()
    }
}
