package com.example.projekt_inz.ui.plan

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlock(block: PlanEntity)

    @Query("DELETE FROM time_blocks WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Update
    suspend fun updateBlock(block: PlanEntity)

    @Delete
    suspend fun deleteBlock(block: PlanEntity)

    @Query("SELECT * FROM time_blocks")
    fun getAllBlocks(): Flow<List<PlanEntity>>

    @Query("DELETE FROM time_blocks")
    suspend fun clearAll()

    @Query("SELECT * FROM time_blocks WHERE dayOfWeek = :day ORDER BY startMinute")
    fun getBlocksForDay(day: Int): Flow<List<PlanEntity>>

    // Overlap detection with minute precision
    @Query("""
        SELECT * FROM time_blocks
        WHERE dayOfWeek = :day
        AND NOT (endMinute <= :start OR startMinute >= :end)
    """)
    suspend fun getOverlappingBlocks(
        day: Int,
        start: Int,
        end: Int
    ): List<PlanEntity>
}