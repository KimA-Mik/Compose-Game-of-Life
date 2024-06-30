package ru.kima.gameoflife.presentation.screens.gameoflife.events

sealed interface GameOfLifeUserEvent {
    data object RunGame : GameOfLifeUserEvent
    data object StopGame : GameOfLifeUserEvent
    data object EditField : GameOfLifeUserEvent
    data object SaveField : GameOfLifeUserEvent
}