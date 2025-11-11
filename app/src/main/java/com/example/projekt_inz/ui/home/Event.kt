package com.example.projekt_inz.ui.home

import java.time.LocalDate
import java.time.LocalTime


class Event (var name: String, var date: LocalDate, var time: LocalTime) {

    companion object {
        var eventsList: ArrayList<Event> = ArrayList()

        fun eventsForDate(date: LocalDate): ArrayList<Event> {
            val events = ArrayList<Event>()

            for (event in eventsList) {
                if (event.date == date)
                    events.add(event)
            }

            return events
        }
    }
}