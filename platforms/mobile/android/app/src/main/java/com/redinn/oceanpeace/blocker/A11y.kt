package com.redinn.oceanpeace.blocker

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class A11y : AccessibilityService() {

    private val TAG = "RecorderService"

    private fun getEventType(event: AccessibilityEvent): String? {
        return when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> "TYPE_WINDOW_STATE_CHANGED"
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> "TYPE_WINDOW_CONTENT_CHANGED"
            AccessibilityEvent.TYPE_WINDOWS_CHANGED -> "TYPE_WINDOWS_CHANGED"
            else -> null
        }
    }

    private fun lauchBlocker() {
        val launchIntent = packageManager.getLaunchIntentForPackage("com.redinn.oceanpeace")
        startActivity(launchIntent) //null pointer check in case package name was not found
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        try {
            Log.v(TAG, getEventType(event) + " " + event.source)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        if (event.packageName == null) {
            Log.e(TAG, event.toString())
            return
        }

        if (AppBlocker.check(event)) lauchBlocker()
    }

    override fun onInterrupt() {
        Log.v(TAG, "onInterrupt")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.v(TAG, "onServiceConnected")
    }
}
