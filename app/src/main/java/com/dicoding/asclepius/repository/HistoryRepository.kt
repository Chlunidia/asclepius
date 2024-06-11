package com.dicoding.asclepius.repository

import android.app.Application
import com.dicoding.asclepius.datasources.local.AsclepiusDatabase
import com.dicoding.asclepius.datasources.local.HistoryEntity

class HistoryRepository(application: Application) {
    private val historyDao = AsclepiusDatabase.getDatabase(application).historyDao()

    suspend fun addHistory(historyEntity: HistoryEntity) {
        historyDao.addHistory(historyEntity)
    }

    fun getHistory(): List<HistoryEntity> = historyDao.getHistory()
}