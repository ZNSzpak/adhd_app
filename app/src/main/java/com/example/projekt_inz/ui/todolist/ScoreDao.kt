package com.example.projekt_inz.ui.todolist

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {

    @Query("SELECT * FROM score WHERE id = 1")
    fun observeScore(): LiveData<ScoreEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(score: ScoreEntity)

    @Query("UPDATE score SET points = points + :value WHERE id = 1")
    suspend fun addPoints(value: Int)

    @Query("SELECT * FROM score WHERE id = 1 LIMIT 1")
    suspend fun getScoreOnce(): ScoreEntity?
}