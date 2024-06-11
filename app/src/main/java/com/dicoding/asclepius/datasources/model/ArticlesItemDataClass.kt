package com.dicoding.asclepius.datasources.model

import com.google.gson.annotations.SerializedName

data class ArticlesItemDataClass(

    @field:SerializedName("publishedAt")
    val publishedAt: String,

    @field:SerializedName("author")
    val author: String,

    @field:SerializedName("urlToImage")
    val urlToImage: String?,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("source")
    val source: SourcesDataClass,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("content")
    val content: String
)
