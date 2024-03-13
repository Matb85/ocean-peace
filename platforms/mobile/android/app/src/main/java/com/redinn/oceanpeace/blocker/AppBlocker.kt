package com.redinn.oceanpeace.blocker

import android.util.Log
import android.view.accessibility.AccessibilityEvent

object AppBlocker {
    private const val TAG = "AppBlocker"
    fun check(event: AccessibilityEvent): Boolean {
        val packageName = event.packageName.toString()
        val index = Utils.appsNames.indexOf(packageName)

        Log.v(TAG, packageName)

        if (index == -1) {
            Log.d(TAG, "app $packageName not recognised")
            return false
        }

        if (Utils.blockingStatuses[index]) {
            Log.d(TAG, "app $packageName blocked")
            return true
        }


        // calc the time spent on the app
        // event.eventTime returns SystemClock.uptimeMillis() !!!
        Log.d(TAG, "TIMESTAMPS: ${event.eventTime} - ${Utils.lastActiveAppTimeStamp}")
        if (Utils.lastActiveApp == packageName) {
            // if the app hasn't change, just update the time
            Utils.timeCounters[index] += event.eventTime - Utils.lastActiveAppTimeStamp
        } else {
            // otherwise, update the time of the previous app and start tracking the current app
            val prevIndex = Utils.appsNames.indexOf(Utils.lastActiveApp)
            if (prevIndex != -1) {
                Utils.timeCounters[prevIndex] += event.eventTime - Utils.lastActiveAppTimeStamp
            }
            Utils.lastActiveApp = packageName
        }

        if (Utils.timeCounters[index] > Utils.timeLimits[index]) {
            Log.d(TAG, "app $packageName is going to be blocked now")
            Utils.blockingStatuses[index] = true
            return true
        }

        Log.d(
            TAG,
            "app $packageName still has time, ${Utils.timeLimits[index] - Utils.timeCounters[index]}  ${Utils.timeLimits[index]} ${Utils.timeCounters[index]}"
        )

        Utils.lastActiveAppTimeStamp = event.eventTime
        return false
    }
}
