package com.redinn.oceanpeace.goals

import com.getcapacitor.JSArray
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.redinn.oceanpeace.database.OceanDatabase
import com.redinn.oceanpeace.database.goals.Goal
import java.util.Objects

@CapacitorPlugin(name = "Goal")
class GoalsPlugin : Plugin() {
    @PluginMethod
    fun saveGoal(call: PluginCall) {
        val retrieved: JSObject = call.getObject("data", JSObject("{}")) as JSObject
        val temp = Goal()
        temp.id = Objects.requireNonNull(retrieved.getString(Goal.ID)).toString()
        temp.name = retrieved.getString(Goal.NAME)!!
        temp.apps = retrieved.getString(Goal.APPS)!!
        temp.websites = retrieved.getString(Goal.WEBSITES)!!
        temp.limit = retrieved.getString(Goal.LIMIT)!!
        temp.activeDays = retrieved.getString(Goal.ACTIVE_DAYS)!!
        temp.type = retrieved.getString(Goal.LIMIT_ACTION_TYPE)!!
        temp.sessionUpdate = ""
        temp.sessionTime = 0
        temp.sessionHistory = retrieved.getString(Goal.SESSION_HISTORY)!!
        OceanDatabase.getDatabase(context.applicationContext).goalDAO().insert(temp)
        call.resolve()
    }

    @PluginMethod
    fun deleteGoal(call: PluginCall) {
        val id = call.getString("id")
        if (id == null) call.reject("id cannot be null")
        val temp = Goal()
        temp.id = id!!
        OceanDatabase.getDatabase(context.applicationContext).goalDAO().delete(temp)
        call.resolve()
    }

    @PluginMethod
    fun getGoal(call: PluginCall) {
        val id = call.getString("id")
        if (id == null) call.reject("id cannot be null")
        val res = OceanDatabase.getDatabase(context).goalDAO().getGoalByName(id)!!.toJSON_forFE()
        call.resolve(res)
    }

    @PluginMethod
    fun getAllGoals(call: PluginCall) {
        val allGoals = OceanDatabase.getDatabase(context).goalDAO().allGoals
        val res = JSObject()
        val array = JSArray()
        for (temp in allGoals!!) array.put(temp!!.toJSON_forFE())
        res.put("goals", array)
        call.resolve(res)
    }
}
