package com.example.projekt_inz.ui.calendar

import java.time.LocalDate

data class CalendarDayUi(
    val date: LocalDate,
    val isToday: Boolean,
    val isSelected: Boolean
)