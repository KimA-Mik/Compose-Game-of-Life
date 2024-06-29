package ru.kima.gameoflife.presentation.screens.gameoflife

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

@Composable
fun GameOfLifeScreen(
    state: ScreenState,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier,
        floatingActionButton = {
            FabControl(state = state.state)
        }) { paddingValues ->
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

@Composable
fun FabControl(
    state: GameOfLifeState,
    modifier: Modifier = Modifier
) {
    when (state) {
        GameOfLifeState.Stopped -> StoppedFab(modifier = modifier)
        GameOfLifeState.Running -> RunningFab(modifier = modifier)
        GameOfLifeState.Editable -> EditableFab(modifier = modifier)
    }
}

@Composable
fun StoppedFab(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FloatingActionButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.edit_fab_content_description)
            )
        }
        FloatingActionButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.Start,
                contentDescription = stringResource(R.string.run_fab_content_description)
            )
        }
    }
}

@Composable
fun RunningFab(modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = { /*TODO*/ },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Stop,
            contentDescription = stringResource(R.string.stop_fab_content_description)
        )
    }
}

@Composable
fun EditableFab(modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = { /*TODO*/ },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Save,
            contentDescription = stringResource(R.string.save_fab_content_description)
        )
    }
}