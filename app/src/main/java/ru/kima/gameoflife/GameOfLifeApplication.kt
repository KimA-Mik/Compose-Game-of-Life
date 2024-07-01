package ru.kima.gameoflife

import android.app.Application
import ru.kima.gameoflife.domain.gameoflife.GameOfLife

class GameOfLifeApplication : Application() {
    val gameOfLife = GameOfLife()
}