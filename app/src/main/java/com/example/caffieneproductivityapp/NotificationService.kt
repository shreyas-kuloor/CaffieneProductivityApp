package com.example.caffieneproductivityapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationService : BroadcastReceiver() {

    private val CHANNEL_ID = "RatingsChannel"
    override fun onReceive(context: Context, intent: Intent) {
        createNotificationChannel(context)
        val nm : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val resultIntent = Intent(context, RateProductivityActivity::class.java)
        val pendingIntent : PendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0)

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.smallcup)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_description))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        nm.notify(0, notificationBuilder.build())

    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val nm : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }
}
