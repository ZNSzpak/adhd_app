package com.example.projekt_inz.ui.home

import java.time.LocalDate
import java.time.LocalTime


data class Event(
    val name: String,
    val date: LocalDate,
    val time: LocalTime
) {
    companion object {
        val eventsList = ArrayList<Event>()

        fun eventsForDate(date: LocalDate): ArrayList<Event> {
            return ArrayList(eventsList.filter { it.date == date })
        }
    }
}