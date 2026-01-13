package com.example.projekt_inz.ui.calendar.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings

object AlarmScheduler {

    fun scheduleEventReminder(
        context: Context,
        triggerAtMillis: Long,
        eventId: Int,
        eventName: String,
        eventTime: String
    ) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Cancel any existing alarm FIRST
        cancelEventReminder(context, eventId)

        val intent = Intent(context, EventReminderReceiver::class.java).apply {
            action = "EVENT_REMINDER_$eventId"
            putExtra("eventId", eventId)
            putExtra("eventName", eventName)
            putExtra("eventTime", eventTime)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            eventId,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !alarmManager.canScheduleExactAlarms()
        ) {
            // Fallback: inexact alarm
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }
    fun cancelEventReminder(context: Context, eventId: Int) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, EventReminderReceiver::class.java).apply {
            action = "EVENT_REMINDER_$eventId"
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            eventId,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }
}