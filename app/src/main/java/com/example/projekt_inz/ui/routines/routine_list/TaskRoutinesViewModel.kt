package com.example.projekt_inz.ui.routines.routine_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TaskRoutinesViewModel (private val repository: TaskRoutinesRepository, private val listId: Long) : ViewModel() {

    val tasks: LiveData<List<TaskEntityRoutines>> = repository.getTasksForList(listId)

    fun addTask(task: TaskEntityRoutines) {
        viewModelScope.launch {
            repository.addTask(task)
        }
    }

    fun updateTask(task: TaskEntityRoutines, newText: String) {
        viewModelScope.launch {
            repository.updateTask(task.copy(text = newText))
        }
    }

    fun toggleTask(task: TaskEntityRoutines, isDone: Boolean) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isDone = isDone))
        }
    }

    fun deleteTask(task: TaskEntityRoutines) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}