package com.example.projekt_inz.ui.routines.routine_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TaskRoutinesViewMFactory (
    private val repository: TaskRoutinesRepository,
    private val listId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskRoutinesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskRoutinesViewModel(repository, listId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}