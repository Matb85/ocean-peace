package com.redinn.oceanpeace.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.redinn.oceanpeace.MainActivity
import com.redinn.oceanpeace.R

object NotificationHelper {
    fun show(context: Context, title: String, content: String, id: Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent,  0 or PendingIntent.FLAG_MUTABLE)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel("default", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Channel Description"
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val notification = Notification.Builder(context, "default")
                    .setTicker("Exceed Usage Limit")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(pendingIntent)
                    .setVisibility(Notification.VISIBILITY_SECRET)
                    .build()

            notificationManager.notify(id, notification)

        } else {

            val notification = Notification.Builder(context)
                    .setTicker("Exceed Usage Limit")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(pendingIntent)
                    .setVisibility(Notification.VISIBILITY_SECRET)
                    .build();

            notificationManager.notify(id, notification)

        }
    }
}
