package com.dicoding.asclepius.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.datasources.model.ArticlesItemDataClass
import com.dicoding.asclepius.service.RetrofitApi
import com.dicoding.asclepius.state.FetchStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ResultViewModel : ViewModel() {
    private val _news = MutableLiveData<FetchStatus<List<ArticlesItemDataClass>>>()
    val news = _news

    init {
        getNews()
    }

    private fun getNews() {
        viewModelScope.launch {
            try {
                val url = RetrofitApi.getApiService().getNews(apiKey = BuildConfig.API_KEY)
                Log.d("ResultViewModel", "Fetching news from: $url")
                _news.postValue(FetchStatus.Loading)
                val response = withContext(Dispatchers.IO) {
                    RetrofitApi.getApiService().getNews(apiKey = BuildConfig.API_KEY)
                }
                if (response.status == "ok") {
                    _news.postValue(FetchStatus.Success(response.articles))
                } else {
                    _news.postValue(FetchStatus.Error("Failed to get news"))
                }
            } catch (e: HttpException) {
                Log.e("ResultViewModel", "Error fetching news: ${e.message()}")
                _news.postValue(FetchStatus.Error(e.message()))
            } catch (e: Exception) {
                Log.e("ResultViewModel", "An error occurred", e)
                _news.postValue(FetchStatus.Error("An error occurred"))
            }
        }
    }
}