package ru.kima.gameoflife.presentation.screens.gameoflife.layout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.unit.toOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.kima.gameoflife.presentation.screens.gameoflife.model.CellItem
import kotlin.math.roundToInt

val CELL_SIZE = 100.dp
private const val PADDING_RATIO = 0.15f

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FieldLayout(
    items: List<CellItem>,
    state: FieldLayoutState,
    modifier: Modifier = Modifier,
    cell: @Composable (cellState: Int) -> Unit
) {

    val cellScale by state.elementScale.collectAsStateWithLifecycle()
    val cellPadding = remember(cellScale) { CELL_SIZE * cellScale * PADDING_RATIO }
    val offsets by state.offsetState.collectAsStateWithLifecycle()

    val itemProvider = rememberItemProvider(items = items) { cellState ->
        Box(modifier = Modifier.size(cellScale * CELL_SIZE)) {
            cell(cellState)
        }
    }

    LazyLayout(
        itemProvider = { itemProvider },
        modifier = modifier.dragFieldLayout(state)
    ) { constraints ->
        val totalCellSize = (cellScale * CELL_SIZE + cellPadding)
        val indexes = itemProvider.getIndexesInConstrains(
            constraints = constraints,
            spaceTaken = totalCellSize.roundToPx(),
            offset = offsets
        )
        val indexesWithPlaceables = indexes.associateWith {
            measure(it, constraints)
        }
        val cellPx = (CELL_SIZE * cellScale).roundToPx()
        val paddingPx = cellPadding.roundToPx()

        layout(constraints.maxWidth, constraints.maxHeight) {
            indexesWithPlaceables.forEach { (index, placeables) ->
                val item = itemProvider.getItem(index)
                item?.let { placeItem(state, item, cellPx, paddingPx, placeables) }
            }
        }
    }
}

fun Placeable.PlacementScope.placeItem(
    state: FieldLayoutState,
    item: CellItem,
    cellsize: Int,
    cellPadding: Int,
    placeables: List<Placeable>
) {
    val xPosition = item.x * (cellsize + cellPadding) - state.offsetState.value.x + cellPadding
    val yPosition = item.y * (cellsize + cellPadding) - state.offsetState.value.y + cellPadding

    placeables.forEach { placeable ->
        placeable.placeRelative(
            xPosition,
            yPosition
        )
    }
}


fun Modifier.dragFieldLayout(state: FieldLayoutState): Modifier {
    return this
        .pointerInput(Unit) {
//            detectDragGestures { change, dragAmount ->
//                change.consume()
//                state.onDrag(IntOffset(dragAmount.x.toInt(), dragAmount.y.toInt()))
//            }

            detectTransformGestures(panZoomLock = true) { centroid, pan, zoom, rotation ->
                val oldScale = state.elementScale.value
                val newScale = oldScale * zoom

                if (zoom == 1.0f) {
                    state.onDrag(IntOffset(pan.x.roundToInt(), pan.y.roundToInt()))
                    return@detectTransformGestures
                }
                val newOffset = (state.offsetState.value.toOffset() + centroid / oldScale) -
                        (centroid / newScale + pan / oldScale)

                state.onZoom(newScale)
                state.onDrag(
                    IntOffset(
                        (-newOffset.x * newScale).toInt(),
                        (-newOffset.y * newScale).toInt()
                    )
                )
            }
        }
}

