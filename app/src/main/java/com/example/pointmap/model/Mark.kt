package com.example.pointmap.model

data class Mark(
    val id: Long,
    val name: String,
    val description: String = "",
    val lat: Double,
    val lon: Double
)
