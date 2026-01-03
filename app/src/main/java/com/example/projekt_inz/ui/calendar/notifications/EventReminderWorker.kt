package com.example.projekt_inz.ui.calendar.notifications

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class EventReminderWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val eventName = inputData.getString("eventName") ?: return Result.failure()
        val eventTime = inputData.getString("eventTime") ?: ""
        val eventId = inputData.getInt("eventId", 0)

        NotificationHelper.showNotification(
            applicationContext,
            eventId,
            "Upcoming Event",
            "$eventName at $eventTime"
        )

        return Result.success()
    }
}