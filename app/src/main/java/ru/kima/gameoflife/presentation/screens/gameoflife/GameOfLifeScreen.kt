package ru.kima.gameoflife.presentation.screens.gameoflife

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Start
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.kima.gameoflife.R
import ru.kima.gameoflife.presentation.screens.gameoflife.events.GameOfLifeUserEvent

@Composable
fun GameOfLifeScreen(
    state: ScreenState,
    modifier: Modifier = Modifier,
    onEvent: (GameOfLifeUserEvent) -> Unit
) {
    Scaffold(modifier = modifier,
        floatingActionButton = {
            FabControl(state = state.state, onEvent = onEvent)
        }) { paddingValues ->
        GameOfLifeContent(
            modifier = Modifier.padding(paddingValues),
            onEvent = onEvent
        )
    }
}

@Composable
fun GameOfLifeContent(
    modifier: Modifier = Modifier,
    onEvent: (GameOfLifeUserEvent) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Game of Life")
    }

}

@Composable
fun FabControl(
    state: GameOfLifeState,
    modifier: Modifier = Modifier,
    onEvent: (GameOfLifeUserEvent) -> Unit
) {
    AnimatedContent(targetState = state, label = "") {
        when (it) {
            GameOfLifeState.Stopped -> StoppedFab(modifier = modifier, onEvent = onEvent)
            GameOfLifeState.Running -> RunningFab(modifier = modifier, onEvent = onEvent)
            GameOfLifeState.Editable -> EditableFab(modifier = modifier, onEvent = onEvent)
        }
    }
}

@Composable
fun StoppedFab(
    modifier: Modifier = Modifier,
    onEvent: (GameOfLifeUserEvent) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FloatingActionButton(onClick = { onEvent(GameOfLifeUserEvent.EditField) }) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.edit_fab_content_description)
            )
        }
        FloatingActionButton(onClick = { onEvent(GameOfLifeUserEvent.RunGame) }) {
            Icon(
                imageVector = Icons.Default.Start,
                contentDescription = stringResource(R.string.run_fab_content_description)
            )
        }
    }
}

@Composable
fun RunningFab(
    modifier: Modifier = Modifier,
    onEvent: (GameOfLifeUserEvent) -> Unit
) {
    FloatingActionButton(
        onClick = { onEvent(GameOfLifeUserEvent.StopGame) },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Stop,
            contentDescription = stringResource(R.string.stop_fab_content_description)
        )
    }
}

@Composable
fun EditableFab(
    modifier: Modifier = Modifier,
    onEvent: (GameOfLifeUserEvent) -> Unit
) {
    FloatingActionButton(
        onClick = { onEvent(GameOfLifeUserEvent.SaveField) },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Save,
            contentDescription = stringResource(R.string.save_fab_content_description)
        )
    }
}