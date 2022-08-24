package com.example.pointmap.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pointmap.model.AppState
import com.example.pointmap.model.Mark
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var _liveData: MutableLiveData<AppState> = MutableLiveData<AppState>(AppState.Data(data = listOf()))
    val liveData: LiveData<AppState> get() = _liveData

    private var _sideEffect: MutableSharedFlow<Point> = MutableSharedFlow()
    val sideEffect: SharedFlow<Point> get() = _sideEffect.asSharedFlow()

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

    fun removeMark(id: Long) {
        val currentState = liveData.value
        if(currentState is AppState.Data) {
            _liveData.value = AppState.Loading
            try {
                val mark = currentState.data.first{ it.id == id }
                _liveData.value = AppState.Data(data = currentState.data - mark)
            } catch (ex: NoSuchElementException) {
                _liveData.value = AppState.Error(message = ex.message ?: DEFAULT_ERROR_MESSAGE)
            }
        }
    }

    fun clearAllMarks() {
        val currentState = liveData.value
        if(currentState is AppState.Data) {
            _liveData.value = AppState.Loading
            _liveData.value = AppState.Data(data = listOf())
        }
    }

    fun moveToSelectedMark(point: Point) {
        viewModelScope.launch { _sideEffect.emit(point) }
    }

    companion object {
        private const val DEFAULT_ERROR_MESSAGE = "Unknown error!"
    }
}