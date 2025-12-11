package com.example.projekt_inz.ui.routines.routine_list

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import com.example.projekt_inz.ui.routines.ButtonListEntry

@Entity(
    tableName = "routine_tasks",
    foreignKeys = [ForeignKey(
        entity = ButtonListEntry::class,
        parentColumns = ["id"],
        childColumns = ["listId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TaskEntityRoutines(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val listId: Long,          // FK to the button/list
    val text: String,
    val isDone: Boolean = false
)