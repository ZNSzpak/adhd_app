package com.example.projekt_inz.ui.calendar.week_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projekt_inz.ui.calendar.EventRepository

class WeekViewModelFactory (
    private val repository: EventRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeekViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeekViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}