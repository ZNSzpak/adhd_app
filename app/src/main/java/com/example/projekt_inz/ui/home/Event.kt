package com.example.projekt_inz.ui.home

import java.time.LocalDate
import java.time.LocalTime

class Event (var name: String, var date: LocalDate, var time: LocalTime) {

    companion object {
        val eventsList = mutableListOf<Event>()
        fun eventsForDate(date: LocalDate): List<Event> {
            return eventsList.filter { it.date == date }
        }
    }
}