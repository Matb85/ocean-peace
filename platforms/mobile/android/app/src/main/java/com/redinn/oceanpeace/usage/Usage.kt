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
import java.text.SimpleDateFormat
import java.util.Locale

object Usage {
    // region GET functions
    const val TAG = "USAGE"
    private fun getManager(context: Context): UsageStatsManager {
        return context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }

    private fun getDayStart(): Long {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 0 // for 6 hour
        calendar[Calendar.MINUTE] = 0 // for 0 min
        calendar[Calendar.SECOND] = 0 // for 0 sec
        Log.d(TAG, calendar.time.toString())
        return calendar.timeInMillis
    }

    data class Stat(
        var totalTime: Long = 0,
        var startTime: Long = 0,
        var resumed: Boolean = false
    )

    private fun allAppsUsageData(context: Context): HashMap<String, Stat> {
        val events =
            getManager(context).queryEvents(
                System.currentTimeMillis() - (Calendar.getInstance().timeInMillis - getDayStart()),
                System.currentTimeMillis()
            )

        val activityTime = HashMap<String, Stat>()

        while (events.hasNextEvent()) {
            val event = UsageEvents.Event()
            events.getNextEvent(event)

            //Log.d(TAG, "${event.packageName}, ${event.timeStamp} ${event.eventType}")
            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                var temp = activityTime[event.packageName]
                if (temp == null) temp = Stat()

                temp.startTime = event.timeStamp
                temp.resumed = true
            } else if (event.eventType == UsageEvents.Event.ACTIVITY_STOPPED) {
                var temp = activityTime[event.packageName]
                if (temp == null) temp = Stat()

                // If app was opened assume it's working time
                if (temp.resumed) {
                    temp.totalTime += event.timeStamp - temp.startTime
                    temp.resumed = false
                }
                activityTime[event.packageName] = temp
            }
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

    fun singleAppUsageData(context: Context, packageName: String): Long {
        val events =
            getManager(context).queryEvents(
                System.currentTimeMillis() - (Calendar.getInstance().timeInMillis - getDayStart()),
                System.currentTimeMillis()
            )

        val temp = Stat()

        while (events.hasNextEvent()) {
            val event = UsageEvents.Event()
            events.getNextEvent(event)
            if (event.packageName != packageName) continue
            //Log.d(TAG, "${event.packageName}, ${event.timeStamp} ${event.eventType}")
            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                temp.startTime = event.timeStamp
                temp.resumed = true
            } else if (event.eventType == UsageEvents.Event.ACTIVITY_STOPPED) {
                // If app was opened assume it's working time
                if (temp.resumed) {
                    temp.totalTime += event.timeStamp - temp.startTime
                    temp.resumed = false
                }
            }
        }

        // Activity is currently running in foreground [edge case]
        if (temp.resumed)
            temp.totalTime += System.currentTimeMillis() - temp.startTime

        return temp.totalTime
    }

    fun getUsageData(context: Context): JSArray {
        val arr = JSArray()
        val icons = OceanDatabase.getDatabase(context).iconDAO().getAllIcons()
        val dataSet = allAppsUsageData(context)
        for (packageName in dataSet.keys) {
            var icon: JSObject
            // receive ICON data
            val data = icons[packageName]
            if (data == null) {
                // TODO: add the "Other" time counter
                continue
            } else {
                icon = data.toJSON()
                if (data.label == "") icon.put("label", "unknown")
            }
            val app = JSObject()
            app.put("minutes", dataSet[packageName]!!.totalTime / 1000 / 60)
            app.put("icon", icon)
            arr.put(app)
        }
        return arr
    }

    fun getTotalTime(context: Context): Long {
        var time: Long = 0
        val data = allAppsUsageData(context)
        for (s in data.values) {
            time += s.totalTime / 1000 / 60
        }
        return time
    }

    const val ONE_HOUR = 1000L * 60 * 60

    fun getUnlockStats(context: Context): JSONArray {
        val hour = Calendar.getInstance()
        Log.d(TAG, SimpleDateFormat("hh:mm aaa", Locale.getDefault()).format(hour.time))

        hour.timeInMillis -= ONE_HOUR * 8  // 10hours back but we want to display the ending time of every period
        Log.d(TAG, SimpleDateFormat("hh:mm aaa", Locale.getDefault()).format(hour.time))

        val endTime = System.currentTimeMillis()
        val startTime =
            endTime - ONE_HOUR * 10
        var iterationTime = startTime
        val stats = JSONArray()
        var key = 0
        while (iterationTime < endTime) {
            val count = countUnlocks(iterationTime, iterationTime + ONE_HOUR * 2, context)
            val record = JSONObject()
            try {
                Log.e(TAG, hour.toString())
                val timeStamp: String =
                    SimpleDateFormat("hh aa", Locale.getDefault()).format(hour.time)
                record.put("hour", timeStamp)
                record.put("key", key)
                record.put("value", count)
            } catch (e: java.lang.Exception) {
                Log.e(TAG, e.printStackTrace().toString())
            }
            stats.put(record)
            key += 10
            hour.timeInMillis += ONE_HOUR * 2
            iterationTime += ONE_HOUR * 2
        }
        Log.i("TEST", "getUnlockStats: $stats")
        return stats
    }

    private fun countUnlocks(startTime: Long, endTime: Long, context: Context): Int {
        val manager = getManager(context)
        var count = 0
        val events = manager.queryEvents(startTime, endTime)
        while (events.hasNextEvent()) {
            val event = UsageEvents.Event()
            events.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.KEYGUARD_HIDDEN) count++
        }
        return count
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


}