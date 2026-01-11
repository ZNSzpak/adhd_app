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

            val maxPosition = repository.getMaxPositionForList(listId) ?: -1

            repository.addTask(
                TaskEntityRoutines(
                    listId = listId,
                    text = newText,
                    position = maxPosition + 1
                )
            )
        }
    }

    fun moveTask(from: Int, to: Int) {
        val currentList = tasks.value?.toMutableList() ?: return

        if (from !in currentList.indices || to !in currentList.indices) return

        // Move the item in the list
        val item = currentList.removeAt(from)
        currentList.add(to, item)

        viewModelScope.launch {
            // Only update tasks whose position changed
            currentList.forEachIndexed { index, task ->
                if (task.position != index) {
                    repository.updateTask(task.copy(position = index))
                }
            }
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