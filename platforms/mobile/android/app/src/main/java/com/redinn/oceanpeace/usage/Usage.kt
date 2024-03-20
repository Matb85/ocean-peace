package com.redinn.oceanpeace.usage

import android.app.usage.UsageEvents
import android.app.usage.UsageStats
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
    private const val TAG = "USAGE"
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
    )

    private fun appsUsageData(
        context: Context,
        appList: List<String>? = null
    ): HashMap<String, Stat> {
        // https://medium.com/@afrinsulthana/building-an-app-usage-tracker-in-android-fe79e959ab26
        val lUsageStatsMap: Map<String, UsageStats> =
            getManager(context).queryAndAggregateUsageStats(
                System.currentTimeMillis() - (Calendar.getInstance().timeInMillis - getDayStart()),
                System.currentTimeMillis()
            )


        val activityTime = HashMap<String, Stat>()

        for (name in lUsageStatsMap.keys) {
            val time = lUsageStatsMap[name]?.totalTimeInForeground
            if (time == null || time <= 1000L * 60) continue
            if (appList != null && !appList.contains(name)) continue
            val temp = Stat()
            temp.totalTime = time
            activityTime[name] = temp
            Log.d(TAG, "NEW TIME $name ${lUsageStatsMap[name]?.totalTimeInForeground}")
        }

        return activityTime
    }


    fun getUsageData(context: Context): JSArray {
        val arr = JSArray()
        val icons = OceanDatabase.getDatabase(context).iconDAO().getAllIcons()
        val dataSet = appsUsageData(context)
        var i = 0
        for (el in dataSet) {
            val icon: JSObject
            // receive ICON data
            val data = icons[el.key]
            if (data == null) {
                // TODO: add the "Other" time counter
                continue
            } else {
                icon = data.toJSON()
                if (data.label == "") icon.put("label", "unknown")
            }
            Log.i(TAG, "getUsageData1: ${el.key} $arr ${el.value.totalTime}")

            val app = JSObject()
            app.put("minutes", el.value.totalTime / 1000 / 60)
            app.put("icon", icon)
            arr.put(app)
            //dataSet.remove(maxBy.key)
            i += 1
        }

        Log.i(TAG, "getUsageData: $arr")
        return arr
    }

    fun getTotalTime(context: Context): Long {
        var time: Long = 0
        val data = appsUsageData(context)
        for (s in data.values) {
            time += s.totalTime / 1000 / 60
        }
        return time
    }

    private const val ONE_HOUR = 1000L * 60 * 60

    fun getUnlockStats(context: Context): JSONArray {
        val hour = Calendar.getInstance()
        hour.timeInMillis -= ONE_HOUR * 8  // 10hours back but we want to display the ending time of every period

        val endTime = System.currentTimeMillis()
        val startTime = endTime - ONE_HOUR * 12
        var iterationTime = startTime
        val stats = JSONArray()
        var key = 0
        Log.d(TAG, "number of unlocks: ${countUnlocks(startTime, endTime, context)}")
        while (iterationTime < endTime) {
            val count = countUnlocks(iterationTime, iterationTime + ONE_HOUR * 2, context)
            val record = JSONObject()
            try {
                val timeStamp: String =
                    SimpleDateFormat("hh aa", Locale.getDefault()).format(hour.time)
                record.put("hour", timeStamp)
                record.put("key", key)
                record.put("value", count)
            } catch (e: java.lang.Exception) {
                Log.e(TAG, e.printStackTrace().toString())
            }
            stats.put(record)
            key += 20
            hour.timeInMillis += ONE_HOUR * 2
            iterationTime += ONE_HOUR * 2
        }
        Log.i("TEST", "getUnlockStats: $stats")
        return stats
    }

    private fun countUnlocks(startTime: Long, endTime: Long, context: Context): Int {
        var count = 0
        val events = getManager(context).queryEvents(startTime, endTime)
        while (events.hasNextEvent()) {
            val event = UsageEvents.Event()
            events.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.KEYGUARD_SHOWN) count++
        }
        return count
    }

    private const val dayDuration = 1000L * 60 * 60 * 24

    fun getScreenTimeHistory(
        context: Context, numberOfDays: Int
    ): JSArray {
        val days = JSArray()
        // https://medium.com/@afrinsulthana/building-an-app-usage-tracker-in-android-fe79e959ab26
        val dayStart = getDayStart()
        Log.i(TAG, "getHistory: $days")

        val daysNames = listOf("Mon", "Tue", "Web", "Thu", "Fri", "Sat", "Sun")

        for (i in 0 until numberOfDays) {
            var totalTime: Long = 0L

            val lUsageStatsMap: Map<String, UsageStats> =
                getManager(context).queryAndAggregateUsageStats(
                    dayStart - dayDuration * (i + 1),
                    dayStart - dayDuration * i
                )
            Log.i(TAG, "getHistory: $i")

            for (name in lUsageStatsMap.keys) {
                val time = lUsageStatsMap[name]?.totalTimeInForeground
                if (time == null || time <= 1000L * 60) continue
                totalTime += time
                Log.i("TEST", "totalTime: $time package: $name")
            }

            val record = JSONObject()
            record.put("time", totalTime / (1000 * 60))
            record.put("day", daysNames[i])
            days.put(record)
        }
        Log.i(TAG, "getHistory: $days")
        return days
    }


}