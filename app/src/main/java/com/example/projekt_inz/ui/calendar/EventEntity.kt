package com.example.projekt_inz.ui.calendar

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val name: String,

    val dateEpochDay: Long,

    val startMinute: Int,  // minutes from 00:00
    val endMinute: Int     // minutes from 00:00
) {
    fun formatTimeRange(): String {
        fun Int.toHHMM(): String = "%02d:%02d".format(this / 60, this % 60)
        return "${startMinute.toHHMM()} - ${endMinute.toHHMM()}"
    }

    fun startTimeMillis(): Long {
        val date = LocalDate.ofEpochDay(dateEpochDay)
        val time = LocalTime.of(startMinute / 60, startMinute % 60)
        val zonedDateTime = ZonedDateTime.of(date, time, ZoneId.systemDefault())
        return zonedDateTime.toInstant().toEpochMilli()
    }
}