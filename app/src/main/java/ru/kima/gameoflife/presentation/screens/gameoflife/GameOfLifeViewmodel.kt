package ru.kima.gameoflife.presentation.screens.gameoflife

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.kima.gameoflife.GameOfLifeApplication
import ru.kima.gameoflife.domain.gameoflife.GameOfLife
import ru.kima.gameoflife.presentation.screens.gameoflife.events.GameOfLifeUserEvent

class GameOfLifeViewmodel(private val gameOfLife: GameOfLife) : ViewModel() {
    private var gameJob: Job? = null

    private val _gameState = MutableStateFlow(GameOfLifeState.Stopped)

    val state = combine(
        _gameState,
        gameOfLife.field
    ) { gameState, field ->
        ScreenState(
            state = gameState,
            fieldWidth = gameOfLife.fieldWidth,
            fieldHeight = gameOfLife.fieldHeight,
            field = field
        )
    }

    fun onEvent(event: GameOfLifeUserEvent) {
        when (event) {
            GameOfLifeUserEvent.EditField -> updateGameState(GameOfLifeState.Editable)
            GameOfLifeUserEvent.RunGame -> updateGameState(GameOfLifeState.Running)
            GameOfLifeUserEvent.SaveField -> updateGameState(GameOfLifeState.Stopped)
            GameOfLifeUserEvent.StopGame -> updateGameState(GameOfLifeState.Stopped)
        }
    }

    private fun updateGameState(state: GameOfLifeState) {
        when (state) {
            GameOfLifeState.Running -> gameJob = runGame()
            else -> gameJob?.cancel()
        }

        _gameState.value = state
    }

    private fun runGame(): Job = viewModelScope.launch(Dispatchers.Default) {
        gameOfLife.gameLoop()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val gameOfLife = (this[APPLICATION_KEY] as GameOfLifeApplication).gameOfLife
                GameOfLifeViewmodel(gameOfLife = gameOfLife)
            }
        }
    }
}