package com.example.projekt_inz.ui.calendar

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("""
        SELECT * FROM events
        WHERE dateEpochDay = :date
        ORDER BY startMinute
    """)
    fun getEventsForDay(date: Long): Flow<List<EventEntity>>

    @Query("""
        SELECT * FROM events
        WHERE dateEpochDay BETWEEN :startDate AND :endDate
        ORDER BY dateEpochDay, startMinute
    """)
    fun getEventsInRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntity) : Long

    @Delete
    suspend fun delete(event: EventEntity)

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteById(id: Int)
}