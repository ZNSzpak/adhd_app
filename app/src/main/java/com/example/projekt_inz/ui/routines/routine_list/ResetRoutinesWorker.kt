package com.example.projekt_inz.ui.routines.routine_list

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ResetRoutinesWorker (
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val db = RoutineTaskDatabase.getDatabase(context) // your Room database
    private val taskDao = db.taskDao() // DAO for routine tasks

    override suspend fun doWork(): Result {
        return try {
            taskDao.resetDoneTasks() // resets all isDone = true tasks
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}