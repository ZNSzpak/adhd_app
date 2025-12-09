package com.example.projekt_inz.ui.routines.routine_list

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDaoRoutines {

    @Query("SELECT * FROM tasks WHERE id = :listId ORDER BY id ASC")
    fun getTasksForList(listId: Long): LiveData<List<TaskEntityRoutines>>

    @Insert
    suspend fun insertTask(task: TaskEntityRoutines): Long

    @Update
    suspend fun updateTask(task: TaskEntityRoutines)

    @Delete
    suspend fun deleteTask(task: TaskEntityRoutines)

    @Query("DELETE FROM tasks WHERE id = :listId")
    suspend fun deleteTasksForList(listId: Long)
}