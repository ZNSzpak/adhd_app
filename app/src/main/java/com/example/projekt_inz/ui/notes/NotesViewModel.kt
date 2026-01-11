package com.example.projekt_inz.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    val allNotes: LiveData<List<NoteEntity>> = repository.allNotes

    fun addNote(entry: NoteEntity) {
        viewModelScope.launch {
            repository.addNotes(entry.text)
        }
    }

    fun deleteNote(entry: NoteEntity) {
        viewModelScope.launch {
            repository.deleteNotes(entry)
        }
    }

    fun updateNote(entry: NoteEntity, newText: String) {
        viewModelScope.launch {
            repository.updateNotes(entry.copy(text = newText))
        }
    }
}