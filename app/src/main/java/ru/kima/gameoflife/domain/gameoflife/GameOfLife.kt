package ru.kima.gameoflife.domain.gameoflife

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.system.measureTimeMillis

private const val TAG = "GameOfLife"

class GameOfLife {
    val fieldWidth = 25
    val fieldHeight = 25
    private var _field = IntArray(fieldWidth * fieldHeight)

    init {
        val centerX = fieldWidth / 2
        val centerY = fieldHeight / 2

        var index = coordinatesToPIndex(centerX, centerY)
        _field[index] = ALIVE
        index = coordinatesToPIndex(centerX + 1, centerY)
        _field[index] = ALIVE
        index = coordinatesToPIndex(centerX, centerY + 1)
        _field[index] = ALIVE
    }


    private fun coordinatesToPIndex(x: Int, y: Int): Int {
        return y * fieldWidth + x
    }

    //TODO: Extract field to stateflow
    fun gameLoop() = flow {
        while (true) {
            val gameTime = measureTimeMillis {
                _field = getNewField()
            }
            Log.i(TAG, "Game tick took $gameTime ms")
            emit(_field.toList())


            if (gameTime < SECOND_MILLIS) {
                delay(SECOND_MILLIS - gameTime)
            } else {
                Log.w(TAG, "Too slow")
            }
        }
    }.flowOn(Dispatchers.Default)

    private fun getNewField(): IntArray {
        val newField = IntArray(_field.size)
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
                if (_field[index] == ALIVE) {
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