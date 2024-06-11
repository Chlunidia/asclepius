package com.dicoding.asclepius.datasources.model

import com.google.gson.annotations.SerializedName

data class NewsDataClass(

    @field:SerializedName("totalResults")
    val totalResults: Int,

    @field:SerializedName("articles")
    val articles: List<ArticlesItemDataClass>,

    @field:SerializedName("status")
    val status: String
)