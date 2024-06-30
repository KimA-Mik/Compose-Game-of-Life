package ru.kima.gameoflife.domain.gameoflife

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.system.measureTimeMillis

private const val TAG = "GameOfLife"

class GameOfLife {
    val fieldWidth = 10
    val fieldHeight = 10
    private var _field = IntArray(fieldWidth * fieldHeight)

    init {
        var index = coordinatesToPIndex(0, 0)
        _field[index] = ALIVE
        index = coordinatesToPIndex(1, 0)
        _field[index] = ALIVE
        index = coordinatesToPIndex(0, 1)
        _field[index] = ALIVE
    }


    private fun coordinatesToPIndex(x: Int, y: Int): Int {
        return y * fieldWidth + x
    }

    fun gameLoop() = flow {
        while (true) {
            Log.i(TAG, "Game tick")
            val gameTime = measureTimeMillis {
                _field = getNewField()
            }
            emit(_field)


            if (gameTime < SECOND_MILLIS) {
                delay(SECOND_MILLIS - gameTime)
            }
        }
    }

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
        else if (coordinate > rightBorder) leftBorder + (coordinate - rightBorder)
        else coordinate
    }

    companion object {
        private const val SECOND_MILLIS = 1000L
        private val ALIVE_CONDITIONS = intArrayOf(2, 3)

        private const val DEAD = 0
        private const val ALIVE = 1
    }
}