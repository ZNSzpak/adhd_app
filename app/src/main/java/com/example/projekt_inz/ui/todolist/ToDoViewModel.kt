package com.example.projekt_inz.ui.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ToDoViewModel(private val repository: TaskRepository) : ViewModel() {

    // Internal mutable list of tasks
    //private val _tasks = MutableLiveData<MutableList<Task>>(mutableListOf())
   // val tasks: LiveData<MutableList<Task>> = _tasks
    val tasks: LiveData<List<TaskEntity>> = repository.tasks

    fun addTask(text: String) {
        viewModelScope.launch {
            repository.addTask(text)
        }
    }

    fun toggleTask(task: TaskEntity, checked: Boolean) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isDone = checked))
        }
    }

    fun updateTask(task: TaskEntity, newText: String) {
        viewModelScope.launch {
            repository.updateTask(task.copy(text = newText))
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    // Add a new task
//    fun addTask(task: Task) {
//        val updatedList = _tasks.value ?: mutableListOf()
//        updatedList.add(task)
//        _tasks.value = updatedList
//    }
//
//    // Update an existing task
//    fun updateTask(position: Int, updatedTask: Task) {
//        val updatedList = _tasks.value ?: mutableListOf()
//        if (position in updatedList.indices) {
//            updatedList[position] = updatedTask
//            _tasks.value = updatedList
//        }
//    }
//
//    // Delete a task
//    fun deleteTask(position: Int) {
//        val updatedList = _tasks.value ?: mutableListOf()
//        if (position in updatedList.indices) {
//            updatedList.removeAt(position)
//            _tasks.value = updatedList
//        }
//    }
//
//    // Toggle a task's completion state
//    fun toggleTask(position: Int, isDone: Boolean) {
//        val updatedList = _tasks.value ?: mutableListOf()
//        if (position in updatedList.indices) {
//            val task = updatedList[position]
//            updatedList[position] = task.copy(isDone = isDone)
//            _tasks.value = updatedList
//        }
//    }
}