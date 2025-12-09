package com.example.projekt_inz.ui.notes

import androidx.lifecycle.LiveData

class NotesRepository(private val dao: NotesDao) {

    val allNotes: LiveData<List<NoteEntity>> = dao.getAllNotes()

    suspend fun addNotes(name: String) {
        dao.insertNote(NoteEntity(text = name))
    }

    suspend fun deleteNotes(note: NoteEntity) {
        dao.deleteNote(note)
    }

    suspend fun updateNotes(note: NoteEntity) {
        dao.updateNote(note)
    }
}