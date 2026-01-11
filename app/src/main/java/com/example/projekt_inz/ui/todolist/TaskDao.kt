package com.example.projekt_inz.ui.todolist

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY position ASC")
    fun getAllTasks(): LiveData<List<TaskEntity>>

    @Query("SELECT MAX(position) FROM tasks")
    suspend fun getMaxPosition(): Int?

    @Insert
    suspend fun insert(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)
}