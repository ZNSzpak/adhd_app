package com.example.projekt_inz.ui.routines

    import android.content.Context
    import androidx.room.Database
    import androidx.room.Room
    import androidx.room.RoomDatabase

    @Database(entities = [ButtonListEntry::class], version = 1, exportSchema = false)
    abstract class RoutinesDatabase : RoomDatabase() {

        abstract fun routinesDao(): RoutinesDao

        companion object {
            @Volatile private var INSTANCE: RoutinesDatabase? = null

            fun getDatabase(context: Context): RoutinesDatabase {
                return INSTANCE ?: synchronized(this) {
                    Room.databaseBuilder(
                        context.applicationContext,
                        RoutinesDatabase::class.java,
                        "todo_list_db"
                    ).build().also { INSTANCE = it }
                }
            }
        }
    }
