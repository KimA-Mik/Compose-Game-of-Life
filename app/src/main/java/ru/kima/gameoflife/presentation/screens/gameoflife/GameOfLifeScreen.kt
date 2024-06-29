package ru.kima.gameoflife.presentation.screens.gameoflife

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun GameOfLifeScreen(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier) { paddingValues ->
        GameOfLifeContent(
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun GameOfLifeContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Game of Life")
    }

}