package com.redinn.oceanpeace.icons

import android.util.Log
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.redinn.oceanpeace.database.OceanDatabase

@CapacitorPlugin(name = "Icons")
class IconsPlugin : Plugin() {
    override fun load() {
        Log.d("IconPlugin", "Starting the instance")
    }

    @PluginMethod
    fun getAllIcons(call: PluginCall) {
        val res = JSObject()
        val icons = OceanDatabase.getDatabase(context).iconDAO().getAllIconsJSON();
        res.put("apps", icons)
        call.resolve(res)
    }

    @PluginMethod
    fun getIcon(call: PluginCall) {
        val packageName = call.getString("packageName")
        if (packageName == null) {
            call.reject("no packageName provided")
            return
        }
        val res = JSObject()

        /* retrieve data */
        val icon = OceanDatabase.getDatabase(context).iconDAO().getIcon(packageName)!!.toJSON()
        res.put("app", icon)
        call.resolve(res)
    }
}
