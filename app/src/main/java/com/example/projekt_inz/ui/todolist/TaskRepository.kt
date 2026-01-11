package com.example.projekt_inz.ui.todolist

class TaskRepository(private val dao: TaskDao) {

    val tasks = dao.getAllTasks()

    suspend fun addTask(entity: TaskEntity) {
        dao.insert(entity)
    }

    suspend fun updateTask(entity: TaskEntity) {
        dao.update(entity)
    }

    suspend fun deleteTask(entity: TaskEntity) {
        dao.delete(entity)
    }

    suspend fun getMaxPosition(): Int? = dao.getMaxPosition()

}