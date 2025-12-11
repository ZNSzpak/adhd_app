package com.example.projekt_inz.ui.plan

data class TimeBlock(
    val id: Long = 0,
    val name: String,
    val day: Int,          // 1 = Mon, 2 = Tue, ... 5 = Fri
    val startHour: Int,    // integer hours: 7–20
    val endHour: Int       // must be > startHour
)