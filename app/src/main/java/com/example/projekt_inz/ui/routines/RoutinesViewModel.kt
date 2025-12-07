package com.example.projekt_inz.ui.routines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekt_inz.ui.todolist.TaskEntity
import com.example.projekt_inz.ui.todolist.TaskRepository
import kotlinx.coroutines.launch

class RoutinesViewModel(private val repository: RoutinesRepository) : ViewModel() {

   // private val dao = RoutinesDatabase.getDatabase(application).todoListDao()


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