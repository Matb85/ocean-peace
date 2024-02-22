package com.redinn.oceanpeace

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class ServiceEndReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("ServiceEndReceiver", "onReceive")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startService(Intent(context, MyService::class.java))
            // TODO: Change this
        } else {
            context.startService(Intent(context, MyService::class.java))
        }
    }
}
