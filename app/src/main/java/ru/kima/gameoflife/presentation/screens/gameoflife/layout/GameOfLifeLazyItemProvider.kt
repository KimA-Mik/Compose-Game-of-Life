package ru.kima.gameoflife.presentation.screens.gameoflife.layout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.kima.gameoflife.presentation.screens.gameoflife.model.CellItem

typealias CellItemContent = @Composable (cellState: Int) -> Unit

@Composable
fun rememberItemProvider(
    items: List<CellItem>,
    layout: CellItemContent
): GameOfLifeLazyItemProvider {
    return remember(items) {
        GameOfLifeLazyItemProvider(items, layout)
    }
}

@OptIn(ExperimentalFoundationApi::class)
class GameOfLifeLazyItemProvider(
    private val items: List<CellItem>,
    private val layout: CellItemContent
) : LazyLayoutItemProvider {
    override val itemCount = items.size

    fun getIndexesInConstrains(
        constraints: Constraints,
        spaceTaken: Int,
        offset: IntOffset
    ): List<Int> {
        val result = mutableListOf<Int>()

        //??
        items.forEachIndexed { index, item ->
            val startX = (spaceTaken * item.x - offset.x)
            val finishX = (startX + spaceTaken)
            val startY = spaceTaken * item.y - offset.y
            val finishY = (startY + spaceTaken)

            if (startX > constraints.maxWidth ||
                startY > constraints.maxHeight ||
                finishX < 0 ||
                finishY < 0
            ) {
                return@forEachIndexed
            }

            result.add(index)
        }
        return result
    }

    fun getItem(index: Int) = items.getOrNull(index)

    @Composable
    override fun Item(index: Int, key: Any) {
        val item = items.getOrNull(index)
        item?.let {
            val state by it.state.collectAsStateWithLifecycle()
            layout(state)
        }
    }
}