package com.example.projekt_inz.ui.todolist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score")
data class ScoreEntity(
    @PrimaryKey val id: Int = 1,
    val points: Int
)