package com.example.projekt_inz.ui.routines

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoutinesDao {

    @Query("SELECT * FROM todo_lists ORDER BY id ASC")
    fun getAllLists(): LiveData<List<ButtonListEntry>>

//    @Query("SELECT * FROM todo_lists WHERE id = :id LIMIT 1")
//    suspend fun getListById(id: Long): ButtonListEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(entry: ButtonListEntry): Long   // return ID!

    @Update
    suspend fun update(entry: ButtonListEntry)

    @Delete
    suspend fun deleteList(entry: ButtonListEntry)
}