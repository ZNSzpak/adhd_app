package com.example.projekt_inz.ui.routines.routine_list

import androidx.lifecycle.LiveData

class TaskRoutinesRepository(private val dao: TaskDaoRoutines) {

    fun getTasksForList(listId: Long): LiveData<List<TaskEntityRoutines>> = dao.getTasksForList(listId)

    suspend fun addTask(task: TaskEntityRoutines) = dao.insertTask(task)

    suspend fun updateTask(task: TaskEntityRoutines) = dao.updateTask(task)

    suspend fun deleteTask(task: TaskEntityRoutines) = dao.deleteTask(task)

    suspend fun getMaxPositionForList(listId: Long): Int? {
        return dao.getMaxPositionForList(listId)
    }
}