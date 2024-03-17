package com.redinn.oceanpeace.focus

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.getcapacitor.JSArray
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.redinn.oceanpeace.focus.FocusService.LocalBinder
import org.json.JSONArray
import org.json.JSONException

@CapacitorPlugin(name = "Focus")
/**
 * TODO: function checking if focus is already running
 * TODO: finish api for FocusService
 */
class FocusPlugin : Plugin() {
    var mService: FocusService? = null
    var mBound = false
    @PluginMethod
    @Throws(JSONException::class)
    fun startStopwatch(call: PluginCall) {
        val c = call.data
        Log.i("T", "startStopwatch: $c")
        var packages = JSONArray()
        try {
            packages = JSONArray(c.getString("packages"))
        } catch (e: NullPointerException) {
            call.reject("Data provided is empty", e)
        }
        if (packages == null || packages.length() == 0) {
            call.reject("Data provided is empty")
        }
        try {
            //MainActivity.connectFocus(getActivity().getApplicationContext());
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val names: MutableList<String> = ArrayList()
        for (i in 0 until packages.length())  // TODO: exception log
            names.add(packages.getString(i))
        try {
            //MainActivity.focusService.startStopwatch(names.toArray(new String[0]));
        } catch (e: Exception) {
            e.printStackTrace()
        }
        call.resolve()
    }

    @PluginMethod
    @Throws(JSONException::class)
    fun startContinuous(call: PluginCall) {
        var packages = JSONArray()
        var _duration = 0L
        try {
            packages = JSONArray(call.getString("packages"))
            Log.i("T", "startContinuous: $packages")
            _duration = call.getInt("duration")!!.toLong()
        } catch (e: NullPointerException) {
            call.reject("Data provided is empty", e)
            return
        }
        if (packages == null || packages.length() == 0) {
            call.reject("Data provided is empty")
            return
        }
        if (_duration <= 0) {
            call.reject("Duration cannot be <0")
            return
        }
        try {
            //MainActivity.connectFocus(getActivity().getApplicationContext());
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val names: MutableList<String> = ArrayList()
        for (i in 0 until packages.length()) {
            // TODO: exception log
            names.add(packages.getString(i))
        }
        try {
            //MainActivity.focusService.startContinuous(names.toArray(new String[0]), _duration);
        } catch (e: Exception) {
            call.reject("Error", e)
        }
    }

    @PluginMethod
    @Throws(JSONException::class)
    fun startPomodoro(call: PluginCall) {
        var packages: JSONArray? = JSONArray()
        var _work: Long = 0
        var _break: Long = 0
        var _cyclesNumber = 0
        try {
            packages = call.getArray("packages", JSArray())
            _work = call.getLong("workDuration")!!
            _break = call.getLong("breakDuration")!!
            _cyclesNumber = call.getInt("cyclesNumber")!!
        } catch (e: NullPointerException) {
            call.reject("Data provided is empty", e)
        }
        if (packages == null || packages.length() == 0) {
            call.reject("Data provided is empty")
        }
        if (_work <= 0 || _break <= 0) {
            call.reject("Duration cannot be <0")
        }
        if (_cyclesNumber < 1) {
            call.reject("Cycles number cannot be <1")
        }


        //MainActivity.connectFocus(getActivity().getApplicationContext());
        val names: MutableList<String> = ArrayList()
        for (i in 0 until packages!!.length()) {
            // TODO: exception log
            names.add(packages.getString(i))
        }
        mService!!.startPomodoro(names.toTypedArray<String>(), _work, _break, _cyclesNumber)
        call.resolve()
    }

    @PluginMethod
    fun stop(call: PluginCall) {
        try {
            //MainActivity.focusService.stop();
        } catch (e: Exception) {
            e.printStackTrace()
        }
        call.resolve()
    }

    private fun connect() {
        val create = Intent().setClass(activity.applicationContext, FocusService::class.java)
        activity.applicationContext.startForegroundService(create)
        val bind = Intent(context.applicationContext, FocusService::class.java)
        context.applicationContext.bindService(bind, connection, Context.BIND_AUTO_CREATE)
    }

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            val binder = service as LocalBinder
            mService = binder.service
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }
}
