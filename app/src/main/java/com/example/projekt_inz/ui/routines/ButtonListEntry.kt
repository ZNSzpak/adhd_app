package com.example.projekt_inz.ui.routines

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_lists")
data class ButtonListEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String
)