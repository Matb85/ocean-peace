package com.redinn.oceanpeace.usage

import android.app.usage.UsageEvents
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import com.getcapacitor.JSArray
import com.getcapacitor.JSObject
import com.redinn.oceanpeace.MainApplication
import com.redinn.oceanpeace.database.OceanDatabase
import com.redinn.oceanpeace.helper.CalendarHelper
import com.redinn.oceanpeace.helper.CsvHelper
import com.redinn.oceanpeace.model.UsageDigest
import com.redinn.oceanpeace.model.UsageRecord
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.Date
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

    private fun getEvents(context: Context): List<UsageEvents.Event> {
        val list: MutableList<UsageEvents.Event> = ArrayList()

        val events =
            getManager(context).queryEvents(getDayStart(), System.currentTimeMillis())
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
    data class Stat(
        var totalTime: Long = 0,
        var startTime: Long = 0,
        var resumed: Boolean = false
    )

    private fun isApp(packageName: String, context: Context): Boolean {
        return try {
            val info = context.packageManager.getApplicationInfo(packageName, 0)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun _applicationsUsageData(context: Context): HashMap<String, Stat> {
        val events = getEvents(context)
        val activityTime = HashMap<String, Stat>()
        Log.e(TAG, "_applicationsUsageData")
        // Activity started previous day
        for (event in events) {
            if (event.eventType == UsageEvents.Event.SCREEN_INTERACTIVE) break
            if (event.eventType != UsageEvents.Event.ACTIVITY_PAUSED) continue
            val s = Stat()
            Log.d(TAG, "${event.packageName}, ${event.timeStamp} ${getDayStart()}")
            s.totalTime += event.timeStamp - getDayStart()
            activityTime[event.packageName] = s
            break
        }

        // Applications usage through the day
        for (event in events) {
            // RESUME event
            Log.d(TAG, "2   ${event.packageName}, ${event.timeStamp} ${getDayStart()}")
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
            time += s.totalTime / 1000 / 60
        }
        return time
    }

    fun getTotalTime2(context: Context): Long {
        val queryUsageStats: List<UsageStats> = getManager(context)
            .queryUsageStats(
                UsageStatsManager.INTERVAL_BEST, getDayStart(),
                System.currentTimeMillis()
            )

        val icons = OceanDatabase.getDatabase(context).iconDAO().getAllIcons()

        var s: Long = 0
        for (stat in queryUsageStats) {

            if (icons[stat.packageName] != null) {
                Log.d(
                    TAG,
                    (icons[stat.packageName]?.label
                        ?: "not found") + "totalTimeForegroundServiceUsed: ${stat.totalTimeForegroundServiceUsed} totalTimeInForeground:  ${stat.totalTimeInForeground} totalTimeVisible: ${stat.totalTimeVisible}"
                )
                s += stat.totalTimeInForeground
            }
        }

        // convert milliseconds to minutes
        return s / 1000 / 60
    }

    class ClonedEvent(event: UsageEvents.Event) {
        var packageName: String
        var eventClass: String
        var timeStamp: Long
        var eventType: Int

        init {
            packageName = event.packageName
            eventClass = event.className
            timeStamp = event.timeStamp
            eventType = event.eventType
        }
    }


    class AppItem {
        var mName: String? = null
        var mPackageName: String? = null
        var mEventTime: Long = 0
        var mUsageTime: Long = 0
        var mEventType = 0
        var mCount = 0
        var mMobile: Long = 0
        var mCanOpen = false
        private var mIsSystem = false
        override fun toString(): String {
            return String.format(
                Locale.getDefault(),
                "name:%s package_name:%s time:%d total:%d type:%d system:%b count:%d",
                mName, mPackageName, mEventTime, mUsageTime, mEventType, mIsSystem, mCount
            )
        }

        fun copy(): AppItem {
            val newItem = AppItem()
            newItem.mName = mName
            newItem.mPackageName = mPackageName
            newItem.mEventTime = mEventTime
            newItem.mUsageTime = mUsageTime
            newItem.mEventType = mEventType
            newItem.mIsSystem = mIsSystem
            newItem.mCount = mCount
            return newItem
        }
    }

    fun getTotalTime3(context: Context): Long {
        val events =
            getManager(context).queryEvents(getDayStart(), System.currentTimeMillis())

        val event = UsageEvents.Event()
        val target = ""
        var prevEndEvent: ClonedEvent? = null
        var start: Long = 0
        val item = AppItem()
        val items: MutableList<AppItem> = mutableListOf()

        while (events.hasNextEvent()) {
            events.getNextEvent(event)
            val currentPackage: String = event.getPackageName()
            val eventType: Int = event.getEventType()
            val eventTime: Long = event.getTimeStamp()
            Log.d(
                TAG,
                "$currentPackage $target" + " " + SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss",
                    Locale.getDefault()
                ).format(Date(eventTime)) + " " + eventType
            )
            if (currentPackage == target) { // 本次交互开始
                Log.d(
                    TAG,
                    "$currentPackage $target" + " " + SimpleDateFormat(
                        "yyyy/MM/dd HH:mm:ss",
                        Locale.getDefault()
                    ).format(Date(eventTime)) + " " + eventType
                )
                // 记录第一次开始时间
                if (eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    Log.d(TAG, "start $start")
                    if (start == 0L) {
                        start = eventTime
                        item.mEventTime = eventTime
                        item.mEventType = eventType
                        item.mUsageTime = 0
                        items.add(item.copy())
                    }
                } else if (eventType == UsageEvents.Event.MOVE_TO_BACKGROUND) { // 结束事件
                    if (start > 0) {
                        prevEndEvent = ClonedEvent(event)
                    }
                    Log.d(TAG, "add end $start")
                }
            } else {
                // 记录最后一次结束事件
                if (prevEndEvent != null && start > 0) {
                    item.mEventTime = prevEndEvent.timeStamp
                    item.mEventType = prevEndEvent.eventType
                    item.mUsageTime = prevEndEvent.timeStamp - start
                    if (item.mUsageTime <= 0) item.mUsageTime = 0
                    if (item.mUsageTime > 5000) item.mCount++
                    items.add(item.copy())
                    start = 0
                    prevEndEvent = null
                }
            }
        }

        var s: Long = 0L
        for (item in items) {
            Log.e(TAG, "${item.mPackageName} ${item.mEventTime} ${item.mUsageTime}")
            s += item.mUsageTime
        }
        return s
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


    data class Result(
        val records: List<UsageRecord> = listOf(),
        val lastEndTime: Long = 0,
        val foregroundPackageName: String? = null
    )

    private var context: WeakReference<Context>? = null
    fun setup(c: Context) {
        context = WeakReference(c)
    }

    const val HOUR_24 = 1000 * 60 * 60 * 24L
    private var mLastForegroundEvent: String? = null
    private var mLastTimeStamp: Long = 0

    fun getLatestEvent(
        usageStatsManager: UsageStatsManager,
        startTime: Long,
        endTime: Long
    ): Result {
        val records = arrayListOf<UsageRecord>()
        val usageEvents = usageStatsManager.queryEvents(startTime, endTime)
        val event = UsageEvents.Event()
        var lastEndTime: Long = 0

        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event)
            val packageName = event.packageName
            val timeStamp = event.timeStamp
            val eventType = event.eventType

            if (eventType == UsageEvents.Event.ACTIVITY_PAUSED && mLastForegroundEvent == packageName && timeStamp > mLastTimeStamp) {
                mLastForegroundEvent?.let {
                    recordUsage(it, mLastTimeStamp, timeStamp - mLastTimeStamp)
                    records.add(
                        UsageRecord(
                            packageName,
                            mLastTimeStamp,
                            timeStamp - mLastTimeStamp
                        )
                    )
                }
                mLastForegroundEvent = null
                lastEndTime = timeStamp
            } else if (eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                mLastForegroundEvent = packageName
                mLastTimeStamp = timeStamp
            }
        }
        return Result(records, lastEndTime, mLastForegroundEvent)
    }

    private fun recordUsage(packageName: String, startTime: Long, duration: Long) {
        val context = context?.get() ?: return
        Log.d(
            "recordUsage",
            "$packageName   from ${CalendarHelper.getDate(startTime)}  -  ${duration / 1000}s"
        )
        val filename = CalendarHelper.getDateCondensed(startTime)
        val path = File(context.filesDir.path + "/" + filename)
        CsvHelper.write(path, listOf(UsageRecord(packageName, startTime, duration)))
    }

    fun queryIntervalUsage(duration: Long): ArrayList<UsageRecord> {
        Log.d("USAGE", "queryIntervalUsage")
        val context = context?.get() ?: return arrayListOf()
        val currentTime = System.currentTimeMillis()
        val startTime = currentTime - duration
        val records = arrayListOf<UsageRecord>()

        for (i in startTime..currentTime step HOUR_24) {
            val filename = CalendarHelper.getDateCondensed(i)
            val file = File(context.filesDir.path + "/" + filename)
            records.addAll(CsvHelper.read(file))
        }
        return records
    }

    private fun filter(records: List<UsageRecord>): List<UsageRecord> {
        val list = MainApplication.store().view.state.notTrackingList
        return records.filter { !list.contains(it.packageName) }
    }

    /**
     * @returns the time spent on the phone in milliseconds
     * */
    fun getTodayUsageTime(filter: Boolean = false): Long {
        val records = if (filter) filter(queryTodayUsage()) else queryTodayUsage()
        return records.sumOf { it.duration }
    }

    fun queryTodayUsage(): List<UsageRecord> {
        return queryUsage(CalendarHelper.getDateCondensed(System.currentTimeMillis()))
    }

    fun queryUsage(day: String): List<UsageRecord> {
        val context = context?.get() ?: return listOf()
        val file = File(context.filesDir.path + "/" + day)
        return CsvHelper.read(file)
    }


    fun query24hUsage(): List<UsageRecord> {
        return queryIntervalUsage(HOUR_24).filter { it.starTime >= System.currentTimeMillis() - HOUR_24 }
    }

    fun getAverageUsageTime(filter: Boolean = false): Long {
        val context = context?.get() ?: return 0
        val ntList = MainApplication.store().view.state.notTrackingList
        val pref = context.getSharedPreferences(UsageDigest.TAG, Context.MODE_PRIVATE)
        val all = if (filter) pref.all.filter { !ntList.contains(it.key) } else pref.all
        var sum = 0L
        var count = 0
        for ((_, value) in all) {
            val t = UsageDigest.fromJson(JSONObject((value as? String).toString())).totalTime
            if (t != 0L) {
                sum += t
                count += 1
            }
        }
        return if (count == 0 || all.isEmpty()) {
            0L
        } else {
            (sum / count)
        }

    }
}