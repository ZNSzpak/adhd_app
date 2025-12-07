package com.example.projekt_inz.ui.routines

import androidx.lifecycle.LiveData
import com.example.projekt_inz.ui.todolist.TaskEntity

class RoutinesRepository(private val dao: RoutinesDao) {

    val allLists: LiveData<List<ButtonListEntry>> = dao.getAllLists()

    suspend fun addList(name: String) {
        dao.insertList(ButtonListEntry(title = name))
    }

    suspend fun deleteList(entry: ButtonListEntry) {
        dao.deleteList(entry)
    }

    suspend fun updateList(entry: ButtonListEntry) {
        dao.update(entry)
    }
}