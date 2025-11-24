package com.example.projekt_inz.ui.home

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object CalendarUtils {

    var selectedDate: LocalDate = LocalDate.now()

    fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    fun daysInWeekArray(selectedDate: LocalDate): ArrayList<LocalDate> {
        val days = ArrayList<LocalDate>()
        var current = selectedDate.with(DayOfWeek.MONDAY)
        repeat(7) {
            days.add(current)
            current = current.plusDays(1)
        }
        return days
    }
}