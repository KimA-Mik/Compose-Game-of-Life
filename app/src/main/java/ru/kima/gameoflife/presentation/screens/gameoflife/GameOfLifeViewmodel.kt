package ru.kima.gameoflife.presentation.screens.gameoflife

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.kima.gameoflife.GameOfLifeApplication
import ru.kima.gameoflife.presentation.screens.gameoflife.events.GameOfLifeUserEvent

class GameOfLifeViewmodel : ViewModel() {
    private val _state = MutableStateFlow(ScreenState())
    val state = _state.asStateFlow()

    fun onEvent(event: GameOfLifeUserEvent) {
        when (event) {
            GameOfLifeUserEvent.EditField -> updateGameState(GameOfLifeState.Editable)
            GameOfLifeUserEvent.RunGame -> updateGameState(GameOfLifeState.Running)
            GameOfLifeUserEvent.SaveField -> updateGameState(GameOfLifeState.Stopped)
            GameOfLifeUserEvent.StopGame -> updateGameState(GameOfLifeState.Stopped)
        }
    }

    private fun updateGameState(state: GameOfLifeState) {
        _state.update {
            it.copy(state = state)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                (this[APPLICATION_KEY] as GameOfLifeApplication)
                GameOfLifeViewmodel()
            }
        }
    }
}