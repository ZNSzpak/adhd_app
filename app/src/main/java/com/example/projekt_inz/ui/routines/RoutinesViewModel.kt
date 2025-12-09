package com.example.projekt_inz.ui.routines

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RoutinesViewModel(private val repository: RoutinesRepository) : ViewModel() {

    val allLists: LiveData<List<ButtonListEntry>> = repository.allLists

    fun addList(title: String) {
        viewModelScope.launch {
            repository.addList(title)
        }
    }

    fun deleteList(entry: ButtonListEntry) {
        viewModelScope.launch {
            repository.deleteList(entry)
        }
    }

    fun updateList(entry: ButtonListEntry, newText: String) {
        viewModelScope.launch {
            repository.updateList(entry.copy(title = newText))
        }
    }
}