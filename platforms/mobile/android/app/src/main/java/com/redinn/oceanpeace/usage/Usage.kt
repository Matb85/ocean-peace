package com.redinn.oceanpeace.usage

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import com.getcapacitor.JSArray
import com.getcapacitor.JSObject
import com.redinn.oceanpeace.database.OceanDatabase
import org.json.JSONArray
import org.json.JSONObject

class Usage {
    // region GET functions
    private fun getManager(context: Context): UsageStatsManager {
        return context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }

    private fun getEvents(context: Context): List<UsageEvents.Event> {
        val list: MutableList<UsageEvents.Event> = ArrayList()
        val events = getManager(context).queryEvents(
            System.currentTimeMillis() - Calendar.getInstance()[Calendar.HOUR_OF_DAY] * 60L * 60L * 1000L - Calendar.getInstance()[Calendar.MINUTE] * 60L * 1000L - Calendar.getInstance()[Calendar.SECOND] * 1000L - Calendar.getInstance()[Calendar.MILLISECOND],
            System.currentTimeMillis()
        )
        while (events.hasNextEvent()) {
            val temp = UsageEvents.Event()
            events.getNextEvent(temp)
            list.add(temp)
        }
        return list
    }

    // endregion
    // region API
    // region USAGE DATA functions
    private inner class Stat {
        var totalTime: Long = 0
        var startTime: Long = 0
        var resumed = false
    }

    private fun isApp(packageName: String, context: Context): Boolean {
        return try {
            val info = context.packageManager.getApplicationInfo(packageName, 0)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun _applicationsUsageData(context: Context): HashMap<String, Stat?> {
        val events = getEvents(context)
        val activityTime = HashMap<String, Stat?>()
        val dayStart =
            System.currentTimeMillis() - Calendar.getInstance()[Calendar.HOUR_OF_DAY] * 60L * 60L * 1000L - Calendar.getInstance()[Calendar.MINUTE] * 60L * 1000L - Calendar.getInstance()[Calendar.SECOND] * 1000L - Calendar.getInstance()[Calendar.MILLISECOND]


        // Activity started previous day
        for (event in events) {
            if (event.eventType == UsageEvents.Event.SCREEN_INTERACTIVE) break
            if (event.eventType != UsageEvents.Event.ACTIVITY_PAUSED) continue
            val s = Stat()
            s.totalTime += event.timeStamp - dayStart
            activityTime[event.packageName] = s
            break
        }

        // Applications usage through the day
        for (event in events) {

            // RESUME event
            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                var temp = activityTime[event.packageName]
                if (temp == null) {
                    temp = Stat()
                }
                temp.startTime = event.timeStamp
                temp.resumed = true
            } else if (event.eventType == UsageEvents.Event.ACTIVITY_PAUSED) {
                var temp = activityTime[event.packageName]
                if (temp == null) {
                    temp = Stat()
                }

                // If app was opened sum it's working time
                if (temp.resumed) {
                    temp.totalTime += event.timeStamp - temp.startTime
                    temp.resumed = false
                }
                activityTime[event.packageName] = temp
            }
            // CLOSE ENOUGH DAMN
        }

        // Activity is currently running in foreground [edge case]
        for (name in activityTime.keys) {
            val stat = activityTime[name]
            if (!stat!!.resumed) continue
            stat.totalTime += System.currentTimeMillis() - stat.startTime
            activityTime[name] = stat
        }
        return activityTime
    }

    // endregion
    fun getUsageData(context: Context): JSArray {
        val ret = JSArray()
        val icons = OceanDatabase.getDatabase(context).iconDAO().getAllIcons()
        val dataSet = _applicationsUsageData(context)
        for (packageName in dataSet.keys) {
            var icon: JSObject

            // receive ICON data
            val data = icons[packageName]
            if (data == null) {
//                icon.put("packageName", "");
//                icon.put("label", "unknown");
//                icon.put("iconPath", "");
//                icon.put("version", "");
                continue
            } else {
                icon = data.toJSON()
                if (data.label == "") icon.put("label", "unknown")
            }
            val app = JSObject()
            app.put("minutes", dataSet[packageName]!!.totalTime / 60000)
            app.put("icon", icon)
            ret.put(app)
        }
        return ret
    }

    fun getTotalTime(context: Context): Long {
        var time: Long = 0
        val data = _applicationsUsageData(context)
        for (s in data.values) {
            time += s!!.totalTime / 1000 / 60
        }
        return time
    }

    fun getUnlockStats(context: Context): JSONArray {
        val startingTime =
            System.currentTimeMillis() - 10 * 60L * 60L * 1000L - Calendar.getInstance()[Calendar.MINUTE] * 60L * 1000L - Calendar.getInstance()[Calendar.SECOND] * 1000L - Calendar.getInstance()[Calendar.MILLISECOND]
        val Stats = JSONArray()
        var iteration_time = startingTime
        var hour = (Calendar.getInstance()[java.util.Calendar.HOUR_OF_DAY] - 10).toByte()
        var key = 0
        while (iteration_time < Calendar.getInstance().timeInMillis) {
            val count = countUnlocks(
                iteration_time,
                iteration_time + 60L * 60 * 1000,
                context
            )
            val record = JSONObject()
            try {
                record.put(
                    "hour",
                    if (hour > 12) (hour - 12).toString() + "pm" else hour.toString() + "am"
                )
                record.put("key", key)
                record.put("value", count)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            Stats.put(record)
            hour++
            key += 10
            iteration_time += 60L * 60L * 1000L
        }
        Log.i("TEST", "getUnlockStats: $Stats")
        return Stats
    }

    // region UNLOCKS functions
    fun countUnlocks(start_time: Long, end_time: Long, context: Context): Int {
        val manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        var count = 0
        val events = manager.queryEvents(
            start_time,
            end_time
        )
        while (events.hasNextEvent()) {
            val _event = UsageEvents.Event()
            events.getNextEvent(_event)
            if (_event.eventType == UsageEvents.Event.KEYGUARD_HIDDEN) {
                count++
            }
        }
        return count
    } // endregion
    // endregion
}