package com.example.projekt_inz.ui.routines

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoutinesDao {

    @Query("SELECT * FROM todo_lists ORDER BY id ASC")
    fun getAllLists(): LiveData<List<ButtonListEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(entry: ButtonListEntry): Long

    @Update
    suspend fun update(entry: ButtonListEntry)

    @Delete
    suspend fun deleteList(entry: ButtonListEntry)
}