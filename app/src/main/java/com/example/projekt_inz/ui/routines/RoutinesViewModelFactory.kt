package com.example.projekt_inz.ui.routines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RoutinesViewModelFactory(private val repository: RoutinesRepository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoutinesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RoutinesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}