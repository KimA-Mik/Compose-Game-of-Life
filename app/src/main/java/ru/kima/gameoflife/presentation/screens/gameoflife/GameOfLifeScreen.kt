package ru.kima.gameoflife.presentation.screens.gameoflife

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Start
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.kima.gameoflife.R
import ru.kima.gameoflife.domain.gameoflife.GameOfLife
import ru.kima.gameoflife.presentation.screens.gameoflife.events.GameOfLifeUserEvent
import ru.kima.gameoflife.presentation.screens.gameoflife.layout.FieldLayout
import ru.kima.gameoflife.presentation.screens.gameoflife.layout.rememberFieldLayoutState

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
            state = state,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            onEvent = onEvent
        )
    }
}

@Composable
fun GameOfLifeContent(
    state: ScreenState,
    modifier: Modifier = Modifier,
    onEvent: (GameOfLifeUserEvent) -> Unit
) {
    if (state.fieldWidth + state.fieldHeight < 1) {
        return
    }

    val layoutState = rememberFieldLayoutState()
    FieldLayout(
        items = state.cells,
        state = layoutState,
        modifier = modifier,
        cellClickable = state.state == GameOfLifeState.Editable,
        cellOnClick = {
            onEvent(GameOfLifeUserEvent.EditCell(it.x, it.y))
        }
    ) { cellState ->
        Cell(state = cellState)
    }
}

@Composable
fun Cell(
    state: Int,
    modifier: Modifier = Modifier
) {
    val color =
        if (state == GameOfLife.ALIVE) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface
    Box(
        modifier = modifier
            .background(color = color)
            .aspectRatio(1f)
    )
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