package com.example.pointmap.ui

import com.example.pointmap.model.Mark

interface ItemClickListener {
    fun onEditClick(name: String, description: String)
    fun onRemoveClick(itemId: Long)
    fun onItemClick(item: Mark)
}