package com.example.projekt_inz.ui.todolist

class TaskRepository(private val dao: TaskDao) {

    val tasks = dao.getAllTasks()

    suspend fun addTask(text: String) {
        dao.insert(TaskEntity(text = text))
    }

    suspend fun updateTask(entity: TaskEntity) {
        dao.update(entity)
    }

    suspend fun deleteTask(entity: TaskEntity) {
        dao.delete(entity)
    }
}