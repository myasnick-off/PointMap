package com.example.pointmap.model

import com.yandex.mapkit.geometry.Point

data class Mark(
    val id: Long,
    val name: String,
    val description: String = "",
    val point: Point
)
