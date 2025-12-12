package com.example.projekt_inz.ui.plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PlanViewModel(
    private val repository: PlanRepository
) : ViewModel() {

    val blocks = repository.allBlocks
    var hourHeight: Int = 0



    fun addBlock(block: PlanEntity) {
        viewModelScope.launch {
            repository.insert(block)
        }
    }

    fun updateBlock(block: PlanEntity) {
        viewModelScope.launch {
            repository.update(block)
        }
    }

    fun deleteBlock(block: PlanEntity) {
        viewModelScope.launch {
            repository.delete(block)
        }
    }

    fun deleteBlockById(id: Int) {
        viewModelScope.launch {
            repository.deleteBlockById(id)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }
}