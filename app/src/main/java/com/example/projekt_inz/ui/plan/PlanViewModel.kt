package com.example.projekt_inz.ui.plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PlanViewModel(
    private val repository: PlanRepository
) : ViewModel() {

    val blocks = repository.allBlocks

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

    fun clearAll() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }
}