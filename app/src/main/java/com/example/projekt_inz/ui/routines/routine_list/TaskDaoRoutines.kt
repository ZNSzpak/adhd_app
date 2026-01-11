package com.example.projekt_inz.ui.routines.routine_list

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDaoRoutines {

    @Query("SELECT * FROM routine_tasks WHERE listId = :listId ORDER BY position  ASC")
    fun getTasksForList(listId: Long): LiveData<List<TaskEntityRoutines>>

    @Query("UPDATE routine_tasks SET isDone = 0 WHERE isDone = 1")
    suspend fun resetDoneTasks()

    @Insert
    suspend fun insertTask(task: TaskEntityRoutines): Long

    @Update
    suspend fun updateTask(task: TaskEntityRoutines)

    @Delete
    suspend fun deleteTask(task: TaskEntityRoutines)

    @Query("DELETE FROM routine_tasks WHERE listId = :listId")
    suspend fun deleteTasksForList(listId: Long)

    @Query("SELECT MAX(position) FROM routine_tasks WHERE listId = :listId")
    suspend fun getMaxPositionForList(listId: Long): Int?
}