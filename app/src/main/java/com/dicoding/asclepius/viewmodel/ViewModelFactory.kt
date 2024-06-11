package com.dicoding.asclepius.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory private constructor(
    private val app: Application
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(app: Application): ViewModelFactory {
            return instance ?: synchronized(ViewModelFactory::class.java) {
                instance ?: ViewModelFactory(app).also { instance = it }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            true -> HistoryViewModel(app) as T
            false -> throw IllegalArgumentException("Unrecognized ViewModel class: ${modelClass.name}")
        }
    }
}
