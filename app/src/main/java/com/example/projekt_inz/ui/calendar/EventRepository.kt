package com.example.projekt_inz.ui.calendar

import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDao: EventDao) {

    fun getEventsForDay(dateEpochDay: Long): Flow<List<EventEntity>> {
        return eventDao.getEventsForDay(dateEpochDay)
    }

    fun getEventsInRange(
        startDateEpochDay: Long,
        endDateEpochDay: Long
    ): Flow<List<EventEntity>> {
        return eventDao.getEventsInRange(startDateEpochDay, endDateEpochDay)
    }

    suspend fun insert(event: EventEntity): Long {
        return eventDao.insert(event)
    }

    suspend fun delete(event: EventEntity) {
        eventDao.delete(event)
    }

    suspend fun deleteById(id: Int) {
        eventDao.deleteById(id)
    }
}