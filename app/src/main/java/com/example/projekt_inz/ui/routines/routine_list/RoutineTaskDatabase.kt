package com.example.projekt_inz.ui.routines.routine_list

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projekt_inz.ui.routines.ButtonListEntry
import com.example.projekt_inz.ui.routines.RoutinesDao

@Database(
    entities = [
        ButtonListEntry::class,
        TaskEntityRoutines::class
    ],
    version = 3,
    exportSchema = false
)
abstract class RoutineTaskDatabase : RoomDatabase() {

    abstract fun routinesDao(): RoutinesDao
    abstract fun taskDao(): TaskDaoRoutines

    companion object {
        @Volatile private var INSTANCE: RoutineTaskDatabase? = null

        fun getDatabase(context: Context): RoutineTaskDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoutineTaskDatabase::class.java,
                    "routine_task_db"
                )
                    .fallbackToDestructiveMigration() // optional
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
