package ru.kima.gameoflife.presentation.screens.gameoflife

data class ScreenState(
    val state: GameOfLifeState = GameOfLifeState.Stopped,
    val fieldWidth: Int = 0,
    val fieldHeight: Int = 0,
    val field: List<Int> = emptyList()
)
