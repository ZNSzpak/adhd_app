package com.example.projekt_inz.ui.todolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ToDoViewModelFactory(private val repository: TaskRepository, private val scoreRepo: ScoreRepository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ToDoViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return ToDoViewModel(repository, scoreRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}