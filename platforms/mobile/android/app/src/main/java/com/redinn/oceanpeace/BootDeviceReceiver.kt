package com.redinn.oceanpeace

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log

class BootDeviceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootDeviceReceiver", "ACTION_BOOT_COMPLETED")
            val serviceComponent = ComponentName(context, MyService::class.java)
            val builder = JobInfo.Builder(0, serviceComponent)
            builder.setOverrideDeadline(5000)
            context.getSystemService(JobScheduler::class.java).schedule(builder.build())

        }
    }
}
