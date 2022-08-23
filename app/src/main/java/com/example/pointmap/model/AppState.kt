package com.example.pointmap.model

sealed class AppState {
    object Loading : AppState()
    data class Data(val data: List<Mark>) : AppState()
    data class Error(val message: String) : AppState()
}
