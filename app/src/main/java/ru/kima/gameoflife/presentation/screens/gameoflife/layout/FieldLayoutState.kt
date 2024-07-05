package ru.kima.gameoflife.presentation.screens.gameoflife.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun rememberFieldLayoutState(): FieldLayoutState {
    return remember { FieldLayoutState() }
}

@Stable
class FieldLayoutState {
    private val _offsetState = MutableStateFlow(Offset(0f, 0f))
    val offsetState = _offsetState.asStateFlow()

    private val _elementScale = MutableStateFlow(1f)
    val elementScale = _elementScale.asStateFlow()

    fun onDrag(offset: Offset) {
        val x = (_offsetState.value.x - offset.x).coerceAtLeast(0f)
        val y = (_offsetState.value.y - offset.y).coerceAtLeast(0f)
        _offsetState.value = Offset(x, y)
    }

    fun setOffset(offset: Offset) {
        val x = offset.x.coerceAtLeast(0f)
        val y = offset.y.coerceAtLeast(0f)
        _offsetState.value = Offset(x, y)
    }

    fun onZoom(newSize: Float) {
        _elementScale.value = newSize.coerceAtLeast(0.1f)//.coerceAtMost(0.7f)
    }
}