package ru.kima.gameoflife.presentation.util

import androidx.compose.ui.Modifier

fun Modifier.conditional(
    condition: Boolean,
    block: Modifier.() -> Modifier
): Modifier {
    if (condition) {
        return this.block()
    }
    return this
}