package ru.kima.gameoflife.domain.gameoflife

import android.util.Log
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlin.system.measureTimeMillis

private const val TAG = "GameOfLife"

class GameOfLife {
    val fieldWidth = 25
    val fieldHeight = 25
    private val _field: MutableStateFlow<List<Int>>
    init {
        val fieldList = MutableList(fieldWidth * fieldHeight) { DEAD }
        val centerX = fieldWidth / 2
        val centerY = fieldHeight / 2

        var index = coordinatesToPIndex(centerX, centerY)
        fieldList[index] = ALIVE
        index = coordinatesToPIndex(centerX + 1, centerY)
        fieldList[index] = ALIVE
        index = coordinatesToPIndex(centerX, centerY + 1)
        fieldList[index] = ALIVE
        _field = MutableStateFlow(fieldList)
    }

    val field = _field.asStateFlow()


    private fun coordinatesToPIndex(x: Int, y: Int): Int {
        return y * fieldWidth + x
    }

    suspend fun gameLoop() = coroutineScope {
        while (isActive) {
            val gameTime = measureTimeMillis {
                _field.value = getNewField()
            }
            Log.i(TAG, "Game tick took $gameTime ms")


            if (gameTime < SECOND_MILLIS) {
                delay(SECOND_MILLIS - gameTime)
            } else {
                Log.w(TAG, "Too slow")
            }
        }
    }

    private fun getNewField(): List<Int> {
        val newField = MutableList(_field.value.size) { DEAD }
        for (y in 0 until fieldHeight) {
            for (x in 0 until fieldWidth) {
                val neighbours = countNeighbours(x, y)
                val isAlive = isCellAlive(neighbours)

                val index = coordinatesToPIndex(x, y)

                newField[index] = if (isAlive) {
                    ALIVE
                } else {
                    DEAD
                }
            }
        }

        return newField
    }

    private fun isCellAlive(neighbours: Int): Boolean {
        for (condition in ALIVE_CONDITIONS) {
            if (condition == neighbours) {
                return true
            }
        }

        return false
    }

    private fun countNeighbours(x: Int, y: Int): Int {
        var res = 0
        for (checkY in y - 1..y + 1) {
            for (checkX in x - 1..x + 1) {
                if (checkY == y && checkX == x) {
                    continue
                }

                val actualX = wrapCoordinate(checkX, 0, fieldWidth)
                val actualY = wrapCoordinate(checkY, 0, fieldHeight)

                val index = coordinatesToPIndex(actualX, actualY)
                if (_field.value[index] == ALIVE) {
                    res += 1
                }
            }
        }

        return res
    }

    private fun wrapCoordinate(coordinate: Int, leftBorder: Int, rightBorder: Int): Int {
        return if (coordinate < leftBorder) rightBorder - (leftBorder - coordinate)
        else if (coordinate >= rightBorder) leftBorder + (coordinate - rightBorder)
        else coordinate
    }

    companion object {
        private const val SECOND_MILLIS = 1000L
        private val ALIVE_CONDITIONS = intArrayOf(2, 3)

        const val DEAD = 0
        const val ALIVE = 1
    }
}