package com.redinn.oceanpeace.blocker

import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import android.util.Log

object Utils {
    @JvmField
    var appsNames: Array<String> = arrayOf()

    @JvmField
    var timeLimits: Array<Long> = arrayOf()

    @JvmField
    var blockingStatuses: Array<Boolean> = arrayOf()

    @JvmField
    var timeCounters: Array<Long> = arrayOf()

    @JvmField
    var lastActiveApp = ""

    @JvmField
    var lastActiveAppTimeStamp: Long = 0

    class Builder {
        fun build(
            appsNames: Array<String>,
            timeLimits: Array<Long>,
            blockingStatuses: Array<Boolean>,
            timeCounters: Array<Long> = arrayOf(),
            lastActiveAppTimeStamp: Long
        ) {
            Utils.appsNames = appsNames
            Utils.timeLimits = timeLimits
            Utils.blockingStatuses = blockingStatuses
            Utils.timeCounters = timeCounters
            Utils.lastActiveAppTimeStamp = lastActiveAppTimeStamp
        }
    }
    

}
