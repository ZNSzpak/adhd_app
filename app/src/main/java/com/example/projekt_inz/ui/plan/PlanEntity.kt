package com.example.projekt_inz.ui.plan

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "time_blocks")
data class PlanEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val name: String,

    // 0 = Monday, 1 = Tuesday, ... 6 = Sunday
    val dayOfWeek: Int,

    val startMinute: Int,  // minutes from 00:00
    val endMinute: Int  ,   // minutes from 00:00
){
    fun formatTimeRange(): String {
        fun Int.toHHMM(): String = "%02d:%02d".format(this / 60, this % 60)
        return "${startMinute.toHHMM()} - ${endMinute.toHHMM()}"
    }
}







