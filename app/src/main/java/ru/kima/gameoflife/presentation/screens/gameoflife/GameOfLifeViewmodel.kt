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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.kima.gameoflife.GameOfLifeApplication
import ru.kima.gameoflife.domain.gameoflife.GameOfLife
import ru.kima.gameoflife.presentation.screens.gameoflife.events.GameOfLifeUserEvent
import ru.kima.gameoflife.presentation.screens.gameoflife.model.CellItem

class GameOfLifeViewmodel(private val gameOfLife: GameOfLife) : ViewModel() {
    private var _gameJob: Job? = null
    private val _gameState = MutableStateFlow(GameOfLifeState.Stopped)
    private val _field: List<CellItem>

    init {
        _field = createField()
        collectField()
    }

    val state = _gameState.map {
        ScreenState(
            state = it,
            fieldWidth = gameOfLife.fieldWidth,
            fieldHeight = gameOfLife.fieldHeight,
            cells = _field
        )
    }

    fun onEvent(event: GameOfLifeUserEvent) {
        when (event) {
            GameOfLifeUserEvent.EditField -> updateGameState(GameOfLifeState.Editable)
            GameOfLifeUserEvent.RunGame -> updateGameState(GameOfLifeState.Running)
            GameOfLifeUserEvent.SaveField -> updateGameState(GameOfLifeState.Stopped)
            GameOfLifeUserEvent.StopGame -> updateGameState(GameOfLifeState.Stopped)
            is GameOfLifeUserEvent.EditCell -> onEditCell(event.x, event.y)
        }
    }

    private fun onEditCell(x: Int, y: Int) {
        gameOfLife.editCell(x, y)
    }

    private fun createField(): List<CellItem> {
        val field = mutableListOf<CellItem>()

        val initialField = gameOfLife.field.value
        for (y in 0 until gameOfLife.fieldWidth) {
            for (x in 0 until gameOfLife.fieldHeight) {
                val cellState = initialField[y * gameOfLife.fieldWidth + x]
                val cell = CellItem(cellState, x, y)
                field.add(cell)
            }
        }
        return field
    }

    private fun collectField() = viewModelScope.launch(Dispatchers.Default) {
        gameOfLife.field.collect { newField ->
            for (y in 0 until gameOfLife.fieldWidth) {
                for (x in 0 until gameOfLife.fieldHeight) {
                    val index = y * gameOfLife.fieldWidth + x
                    val cellState = newField[y * gameOfLife.fieldWidth + x]
                    _field[index].setCellState(cellState)
                }
            }
        }
    }

    private fun updateGameState(state: GameOfLifeState) {
        when (state) {
            GameOfLifeState.Running -> _gameJob = runGame()
            else -> _gameJob?.cancel()
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