package com.example.pointmap

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pointmap.model.AppState
import com.example.pointmap.model.Mark
import com.yandex.mapkit.geometry.Point

class MainViewModel : ViewModel() {
    private var _liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()
    val liveData: LiveData<AppState> get() = _liveData

    fun addMark(point: Point) {
        val currentState = liveData.value
        if(currentState is AppState.Data) {
            _liveData.value = AppState.Loading
            val newMark = Mark(
                id = System.currentTimeMillis(),
                name = "Mark ${currentState.data.size}",
                point = point
            )
            _liveData.value = AppState.Data(data = currentState.data + newMark)
        }
    }
}