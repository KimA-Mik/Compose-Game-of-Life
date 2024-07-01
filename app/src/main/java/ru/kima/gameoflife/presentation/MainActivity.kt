package ru.kima.gameoflife.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.kima.gameoflife.presentation.screens.gameoflife.GameOfLifeScreen
import ru.kima.gameoflife.presentation.screens.gameoflife.GameOfLifeViewmodel
import ru.kima.gameoflife.presentation.screens.gameoflife.ScreenState
import ru.kima.gameoflife.presentation.screens.gameoflife.events.GameOfLifeUserEvent
import ru.kima.gameoflife.presentation.ui.theme.GameOfLifeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewmodel: GameOfLifeViewmodel = viewModel(factory = GameOfLifeViewmodel.Factory)
            val state by viewmodel.state.collectAsStateWithLifecycle(ScreenState())
            val onEvent = remember<(GameOfLifeUserEvent) -> Unit> {
                {
                    viewmodel.onEvent(it)
                }
            }

            GameOfLifeTheme {
                GameOfLifeScreen(state = state, onEvent = onEvent)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GameOfLifeTheme {
        Greeting("Android")
    }
}