package com.example.projekt_inz.ui.plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PlanViewModelFactory(
    private val repository: PlanRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlanViewModel::class.java)) {
            return PlanViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}