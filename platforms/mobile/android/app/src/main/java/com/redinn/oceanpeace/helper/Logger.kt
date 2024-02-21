package com.redinn.oceanpeace.helper

import android.util.Log

@Suppress("unused")
object Logger {
    fun d(tag: String, msg: String) {
        /*if (BuildConfig.DEBUG)*/ Log.d(tag, msg)
    }

    fun e(tag: String, msg: String) {
        Log.e(tag, msg)
    }
}
