package com.example.projekt_inz.ui.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ToDoViewModel(
    private val repository: TaskRepository,
    private val scoreRepo: ScoreRepository
    ) : ViewModel() {

    val tasks: LiveData<List<TaskEntity>> = repository.tasks
    val score = scoreRepo.score

    init {
        viewModelScope.launch {
            scoreRepo.initIfNeeded()
        }
    }

    fun addTask(text: String) {
        viewModelScope.launch {
            val maxPosition = repository.getMaxPosition() ?: -1
            val task = TaskEntity(text = text, position = maxPosition + 1)
            repository.addTask(task)
        }
    }

    fun toggleTask(task: TaskEntity, checked: Boolean) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isDone = checked))
            if (checked) {
                scoreRepo.addPoints(10)
            }
        }
    }

    fun updateTask(task: TaskEntity, newText: String) {
        if (task.text == newText) return
        viewModelScope.launch {
            repository.updateTask(task.copy(text = newText))
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun moveTask(from: Int, to: Int) {
        val currentList = tasks.value?.toMutableList() ?: return
        if (from !in currentList.indices || to !in currentList.indices) return

        val item = currentList.removeAt(from)
        currentList.add(to, item)

        viewModelScope.launch {
            currentList.forEachIndexed { index, task ->
                if (task.position != index) {
                    repository.updateTask(task.copy(position = index))
                }
            }
        }
    }
}