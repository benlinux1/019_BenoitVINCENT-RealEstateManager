package com.benlinux.realestatemanager.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.benlinux.realestatemanager.R

// Create notification channel for API 26+
// Useful for user who want to configure notifications settings on his device
fun createNotificationChannel(context: Context, channelId: String ) {
    val channelName = context.resources.getString(R.string.app_name)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT).apply {
            lightColor = Color.BLUE
            enableLights(true)
        }
        val manager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}

@SuppressLint("MissingPermission", "UnspecifiedImmutableFlag")
fun sendCreationNotification(notificationText: String,
                             context: Context,
                             permissionGranted: Boolean,
                             notificationIntent: Intent) {

    // Create channel
    val channelId = context.resources.getString(R.string.app_name)
    createNotificationChannel(context, channelId)

    // Define intent with flag that update current activity if app is open
    val contentIntent: PendingIntent? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        // For API 31+
        PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    } else {
        // For API 30 and less
        PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    // Define notification text message
    val notificationMessage = buildString {
        append(context.resources.getString(R.string.notification_message))
        append(notificationText)
    }

    // Build notification attributes
    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle(context.resources.getString(R.string.notification_title))
        .setContentText(notificationMessage)
        .setSmallIcon(R.mipmap.drawer_logo)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(contentIntent)
        .setAutoCancel(false)
        .setOnlyAlertOnce(true)
        .build()


    val notificationManager = NotificationManagerCompat.from(context)

    // Notify user
    if (permissionGranted) {
        notificationManager.notify(1, notification)
    }
}