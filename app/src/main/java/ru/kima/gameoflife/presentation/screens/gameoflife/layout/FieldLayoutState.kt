package ru.kima.gameoflife.presentation.screens.gameoflife.layout

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun rememberFieldLayoutState(): FieldLayoutState {
    return remember { FieldLayoutState() }
}

private const val TAG = "FieldLayoutState"

@Stable
class FieldLayoutState {
    private val _offsetState = MutableStateFlow(IntOffset(0, 0))
    val offsetState = _offsetState.asStateFlow()

    private val _elementScale = MutableStateFlow(0.5f)
    val elementScale = _elementScale.asStateFlow()

    fun onDrag(offset: IntOffset) {
        val x = (_offsetState.value.x - offset.x).coerceAtLeast(0)
        val y = (_offsetState.value.y - offset.y).coerceAtLeast(0)
        _offsetState.value = IntOffset(x, y)
        Log.d(TAG, _offsetState.value.toString())
    }

    fun onZoom(newSize: Float) {
        _elementScale.value = newSize.coerceAtLeast(0.1f).coerceAtMost(0.7f)
    }
}