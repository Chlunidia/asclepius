package com.dicoding.asclepius.datasources.model

import com.google.gson.annotations.SerializedName

data class SourcesDataClass(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: Any
)