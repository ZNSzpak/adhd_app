package com.example.projekt_inz.ui.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ToDoViewModel(
    private val repository: TaskRepository,
    private val scoreRepo: ScoreRepository
    ) : ViewModel() {

    val tasks: LiveData<List<TaskEntity>> = repository.tasks
    val score = scoreRepo.score

    init {
        viewModelScope.launch {
            scoreRepo.initIfNeeded()
        }
    }

    fun addTask(text: String) {
        viewModelScope.launch {
            repository.addTask(text)
        }
    }

    fun toggleTask(task: TaskEntity, checked: Boolean) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isDone = checked))
            if (checked) {
                scoreRepo.addPoints(10)
            }
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