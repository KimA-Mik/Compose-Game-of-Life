package ru.kima.gameoflife.presentation.screens.gameoflife.model

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


@Stable
class CellItem(
    cellState: Int,
    val x: Int,
    val y: Int,
) {
    private val _state = MutableStateFlow(cellState)
    val state = _state.asStateFlow()

    fun setCellState(cellState: Int) {
        _state.value = cellState
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CellItem

        if (x != other.x) return false
        if (y != other.y) return false
        if (_state.value != other._state.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + _state.value
        return result
    }
}