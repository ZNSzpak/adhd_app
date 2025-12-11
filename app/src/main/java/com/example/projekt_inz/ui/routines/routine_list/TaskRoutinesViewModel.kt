package com.example.projekt_inz.ui.routines.routine_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TaskRoutinesViewModel (private val repository: TaskRoutinesRepository, private val listId: Long) : ViewModel() {

    val tasks: LiveData<List<TaskEntityRoutines>> = repository.getTasksForList(listId)

//    fun addTask(newText: String) {
//        viewModelScope.launch {
//            val task = TaskEntityRoutines(
//                listId = listId,
//                text = newText
//            )
//            repository.addTask(task)
//        }
//        Log.d("ADD_TASK", "Adding task to listId = $listId")
//    }

    fun addTask(newText: String) {
        viewModelScope.launch {
            repository.addTask(
                TaskEntityRoutines(
                    listId = listId,
                    text = newText
                )
            )
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