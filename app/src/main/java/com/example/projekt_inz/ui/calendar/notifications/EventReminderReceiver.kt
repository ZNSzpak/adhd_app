package com.example.projekt_inz.ui.calendar.notifications

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

class EventReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        val eventName = intent.getStringExtra("eventName") ?: return
        val eventTime = intent.getStringExtra("eventTime") ?: ""
        val eventId = intent.getIntExtra("eventId", 0)

        NotificationHelper.showNotification(
            context,
            eventId,
            "Upcoming Event",
            "$eventName at $eventTime"
        )
    }
}