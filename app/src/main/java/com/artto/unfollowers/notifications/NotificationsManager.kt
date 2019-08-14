package com.artto.unfollowers.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.artto.unfollowers.R
import com.artto.unfollowers.ui.MainActivity

class NotificationsManager(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "Unfollowers"
        private const val lightColor = Color.WHITE
        private val vibrationPattern = longArrayOf(2000)
    }

    init {
        createNotificationChannel()
    }

    fun showNewUnfollowersNotification(count: Int) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setChannelId(CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setLights(lightColor, 2000, 2000)
                .setVibrate(vibrationPattern)
                .setContentTitle(context.getString(R.string.new_unfollowers_notification, count))
                .setContentIntent(getPendingIntent())
                .setAutoCancel(true)
                .build()

        NotificationManagerCompat.from(context).notify(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
                val id = CHANNEL_ID
                val name = CHANNEL_ID
                val importance = NotificationManager.IMPORTANCE_HIGH

                NotificationChannel(id, name, importance).let {
                    it.vibrationPattern = vibrationPattern
                    it.enableVibration(true)

                    it.lightColor = lightColor
                    it.enableLights(true)

                    createNotificationChannel(it)
                }
            }
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return PendingIntent.getActivity(context, 0, intent, 0)
    }

}