package com.example.projekt_inz.ui.todolist

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class ScoreRepository(private val scoreDao: ScoreDao) {

    val score = scoreDao.observeScore()

    suspend fun addPoints(points: Int) {
        scoreDao.addPoints(points)
    }

    suspend fun initIfNeeded() {
        if (scoreDao.getScoreOnce() == null) {
            scoreDao.insert(ScoreEntity(points = 0))
        }
    }
}