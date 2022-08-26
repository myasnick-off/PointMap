package com.example.pointmap.ui.list

import com.yandex.mapkit.geometry.Point

interface ItemClickListener {
    fun onEditClick(itemId: Long)
    fun onRemoveClick(itemId: Long)
    fun onItemClick(point: Point)
}