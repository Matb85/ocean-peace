package com.redinn.oceanpeace.focus

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.SystemClock
import androidx.work.Data
import androidx.work.ListenableWorker
import com.redinn.oceanpeace.R

/**
 * Finish focus:
 * TODO: create communication with MayoAPI
 * TODO: make notification
 * TODO: add continuous
 * TODO: add pomodoro
 * TODO: add stopwatch
 * TODO: run tests
 */
class FocusService : Service() {
    var handlerThread = HandlerThread("ocean.focus.handler.thread")
    var looper = handlerThread.looper
    var mHandler: Handler? = null
    override fun onCreate() {
        handlerThread = HandlerThread("ocean.focus.handler.thread")
        looper = handlerThread.looper
        handlerThread.start()
        mHandler = Handler()
        super.onCreate()
    }

    override fun onDestroy() {
        handlerThread.quitSafely()
        notificationManager!!.deleteNotificationChannel("911")
        super.onDestroy()
    }

    /**
     * [array][java.lang.reflect.Array] of [String] containing PackageNames of apps selected for blocking in focus session.
     *
     * @see .isBlocked
     * @since 5.11.2022, [FocusService] ver 2.0
     */
    var appsPackageNames: Array<String>? = null

    /**
     * Function checking if provided *packageName* is in the [.appsPackageNames] array.
     * This is the basic form of search. Function is checking every element of [.appsPackageNames] if is equal to *packageName*.
     *
     * @param packageName [String] containing packageName to search for
     * @return *true* - [.appsPackageNames] contains *packageName* <br></br> *false* - [.appsPackageNames] doesn't contain *packageName*
     * @see .appsPackageNames
     *
     * @since 5.11.2022, [FocusService] ver 2.0
     */
    fun isBlocked(packageName: String): Boolean {
        for (i in appsPackageNames!!) {
            if (i == packageName) return true
        }
        return false
    }

    /**
     * [Bolean][Boolean] of current state of focus. <br></br>
     * If *true* focus session is currently running. <br></br>
     * If *false* there isn't any running focus session.
     *
     * @see FocusService
     *
     * @since 5.11.2022, [FocusService] ver 2.0
     */
    var isRunning = false

    /**
     * Stopwatch is a focus session type that runs endless until user stop it or process will be killed.
     * It blocks all preselected applications. Responsive for blocking is
     * Function first check if other focus session isn't running, then overrides [.appsPackageNames] array and send broadcast to Mayo to start blocking selected apps.
     *
     * @param apps [array][java.lang.reflect.Array] of [String] containing PackageNames of apps selected for blocking in focus session
     * @return [Result][androidx.work.ListenableWorker.Result]. If *failure*, a error message is provided. If *true* focus session started correctly
     * @see .appsPackageNames
     *
     * @see .startContinuous
     * @see .startPomodoro
     * @see .stop
     * @since 5.11.2022, [FocusService] ver 2.0
     */
    fun startStopwatch(apps: Array<String>): ListenableWorker.Result {
        // Check if the other session isn't running
        if (isRunning) {
            val returnData = Data.Builder().putString(FIELD_RESULT, ERROR_RUNNING).build()
            return ListenableWorker.Result.failure(returnData)
        }
        try {
            // override the array
            appsPackageNames = apps

            //blocking notifications
            val notificationManager = applicationContext.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY)

            // send broadcast to Mayo
            broadcastStart()
        } catch (e: Exception) {
            e.printStackTrace()
            val returnData = Data.Builder().putString(FIELD_RESULT, ERROR_DATA).build()
            return ListenableWorker.Result.failure(returnData)
        }
        isRunning = true
        return ListenableWorker.Result.success()
    }

    /**
     * Continuous is a focus session that runs for chosen by user duration.
     * It blocks all preselected applications. Responsive for blocking is
     * Function first checks if other focus session isn't running, then overrides [.appsPackageNames] array and setups a handler's [Runnable] to run after timeout.
     * [Runable][Runnable] runs [stop][FocusService.stop] function.
     * At the end function sends broadcast to Mayo to start blocking selected apps.
     *
     * @param apps           [array][java.lang.reflect.Array] of [String] containing PackageNames of apps selected for blocking in focus session
     * @param durationMillis duration of focus session provided in milliseconds
     * @return returns [Result][androidx.work.ListenableWorker.Result]. If *failure*, a error message is provided. If *true* focus session started correctly
     * @see .appsPackageNames
     *
     * @see .startContinuous
     * @see .startPomodoro
     * @see .stop
     * @since 5.11.2022, [FocusService] ver 2.0
     */
    fun startContinuous(apps: Array<String>, durationMillis: Long): ListenableWorker.Result {
        // Check if the other focus session isn't running
        if (isRunning) {
            val returnData = Data.Builder().putString(FIELD_RESULT, ERROR_RUNNING).build()
            return ListenableWorker.Result.failure(returnData)
        }
        try {
            // override the array
            appsPackageNames = apps

            //blocking notifications
            val notificationManager = applicationContext.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY)

            // post a handler Runnable to stop the session after timeout
            mHandler!!.postAtTime(
                { stop() },
                SystemClock.uptimeMillis() + durationMillis
            )

            // send broadcast to Mayo
            broadcastStart()
        } catch (e: Exception) {
            e.printStackTrace()
            val returnData = Data.Builder().putString(FIELD_RESULT, ERROR_DATA).build()
            return ListenableWorker.Result.failure(returnData)
        }
        isRunning = true
        return ListenableWorker.Result.success()
    }

    private var cyclesCount = 1

    /**
     * Pomodoro is a type of Focus session containing two phases running alternately: work and break. One full cycle consist of one work phase and one break phase,
     * except last work phase which after which there is no break phase and the session ends.
     * Function at the beginning checks if there is no other focus sessions running, then updates [.appsPackageNames] array,
     * blocks notifications and starts work phase [Runnable] which will handle the lifecycle of session.
     * After the specified number of cycles will end the function will call [.stop] function.
     *
     * @param apps                 [array][java.lang.reflect.Array] of [String] containing PackageNames of apps selected for blocking in focus session
     * @param workDurationInMills  duration of work phases, provided in milliseconds
     * @param breakDurationInMills duration of break phases, provided in milliseconds
     * @param numberOfCycle        number of cycles
     * @return [Result][androidx.work.ListenableWorker.Result]. If *failure*, a error message is provided. If *true* focus session started correctly
     * @see .appsPackageNames
     *
     * @see .startContinuous
     * @see .startStopwatch
     * @see .stop
     * @since 9.11.2022, [FocusService] ver 2.0
     */
    fun startPomodoro(
        apps: Array<String>,
        workDurationInMills: Long,
        breakDurationInMills: Long,
        numberOfCycle: Int
    ): ListenableWorker.Result {

        // Check if the other session isn't running
        if (isRunning) {
            val returnData = Data.Builder().putString(FIELD_RESULT, ERROR_RUNNING).build()
            return ListenableWorker.Result.failure(returnData)
        }
        try {
            // override the array
            appsPackageNames = apps
            setupWorkingPhase(workDurationInMills, breakDurationInMills, numberOfCycle.toLong())

            //blocking notifications
            val notificationManager = applicationContext.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY)

            // send broadcast to Mayo
            broadcastStart()
        } catch (e: Exception) {
            e.printStackTrace()
            val returnData = Data.Builder().putString(FIELD_RESULT, ERROR_DATA).build()
            return ListenableWorker.Result.failure(returnData)
        }
        isRunning = true
        return ListenableWorker.Result.success()
    }

    private fun setupWorkingPhase(workDuration: Long, breakDuration: Long, numberOfCycles: Long) {
        // check if the index of cycle reached the limit
        if (cyclesCount >= numberOfCycles) {
            stop()
            return
        }
        mHandler!!.postAtTime(
            { // start break phase
                setupBreakPhase(breakDuration, workDuration, numberOfCycles)
            },
            SystemClock.uptimeMillis() + workDuration
        )
    }

    private fun setupBreakPhase(breakDuration: Long, workDuration: Long, numberOfSessions: Long) {
        mHandler!!.postAtTime(
            { // index of incoming cycle
                cyclesCount++
                //start work phase
                setupWorkingPhase(workDuration, breakDuration, numberOfSessions)
            },
            SystemClock.uptimeMillis() + breakDuration
        )
    }

    fun stop() {
        // make array empty
        appsPackageNames = arrayOf()

        //blocking notifications
        val notificationManager = applicationContext.getSystemService(
            NotificationManager::class.java
        )
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
        notificationManager.cancelAll()

        // remove all queued handler Runnables
        mHandler!!.removeCallbacksAndMessages(null)
        isRunning = false
        cyclesCount = 1
        stopSelf()
    }

    /**
     * Function sending broadcast for Mayo to bind this service
     */
    private fun broadcastStart() {}
    private val binder: IBinder = LocalBinder()

    inner class LocalBinder : Binder(), IBinder {
        val service: FocusService
            get() =// Return this instance of LocalService so clients can call public methods
                this@FocusService
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    private var notificationManager: NotificationManager? = null
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notificationManager = applicationContext.getSystemService(
            NotificationManager::class.java
        )
        notificationManager.createNotificationChannel(
            NotificationChannel(
                "911",
                "oceanpeace.focus",
                NotificationManager.IMPORTANCE_HIGH
            )
        )
        val notificationIntent = Intent(this, FocusService::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = Notification.Builder(this, "911")
            .setContentTitle("Focus session is running")
            .setSmallIcon(R.drawable.splash) //.setContentIntent(pendingIntent)
            .build()
        startForeground(912, notification)
        return super.onStartCommand(intent, flags, startId)
    }

    companion object {
        const val ERROR_RUNNING = "Focus is running"
        const val ERROR_DATA = "Data error occurred"
        const val FIELD_RESULT = "result"
    }
}
