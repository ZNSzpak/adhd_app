package com.example.projekt_inz.ui.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ToDoViewModel(private val repository: TaskRepository) : ViewModel() {

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

}