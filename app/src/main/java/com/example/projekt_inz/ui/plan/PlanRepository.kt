package com.example.projekt_inz.ui.plan

import kotlinx.coroutines.flow.Flow

class PlanRepository (private val dao: PlanDao) {

    val allBlocks: Flow<List<PlanEntity>> = dao.getAllBlocks()

    suspend fun deleteBlockById(id: Int) {
        dao.deleteById(id)
    }

    suspend fun insert(block: PlanEntity) =
        dao.insertBlock(block)

    suspend fun update(block: PlanEntity) =
        dao.updateBlock(block)

    suspend fun delete(block: PlanEntity) =
        dao.deleteBlock(block)

    suspend fun clearAll() =
        dao.clearAll()
}