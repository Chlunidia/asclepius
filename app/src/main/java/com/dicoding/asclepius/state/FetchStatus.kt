package com.dicoding.asclepius.state

sealed class FetchStatus<out R> private constructor() {
    data class Success<out T>(val data: T) : FetchStatus<T>()
    data class Error(val error: String) : FetchStatus<Nothing>()
    data object Loading : FetchStatus<Nothing>()
}