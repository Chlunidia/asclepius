package com.dicoding.asclepius.service

import com.dicoding.asclepius.datasources.model.NewsDataClass
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything")
    suspend fun getNews(
        @Query("q") query: String = "cancer",
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String
    ): NewsDataClass
}
