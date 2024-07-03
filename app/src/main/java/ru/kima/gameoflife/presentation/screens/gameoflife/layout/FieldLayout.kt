package ru.kima.gameoflife.presentation.screens.gameoflife.layout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.kima.gameoflife.presentation.screens.gameoflife.model.CellItem
import kotlin.math.roundToInt

val CELL_SIZE = 48.dp
private const val PADDING_RATIO = 0.15f

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FieldLayout(
    items: List<CellItem>,
    state: FieldLayoutState,
    modifier: Modifier = Modifier,
    cellOnClick: CellItemOnClick,
    cell: @Composable (cellState: Int) -> Unit
) {

    val cellScale by state.elementScale.collectAsStateWithLifecycle()
    val cellPadding = remember(cellScale) { CELL_SIZE * cellScale * PADDING_RATIO }
    val offsets by state.offsetState.collectAsStateWithLifecycle()

    val itemProvider = rememberItemProvider(items = items) { cell ->
        val cellState by cell.state.collectAsStateWithLifecycle()
        Box(modifier = Modifier
            .size(cellScale * CELL_SIZE)
            .clickable { cellOnClick(cell) }) {
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
            offset = IntOffset(offsets.x.roundToInt(), offsets.y.roundToInt())
        )
        val indexesWithPlaceables = indexes.associateWith {
            measure(it, constraints)
        }
        val cellPx = (CELL_SIZE * cellScale).roundToPx()
        val paddingPx = cellPadding.roundToPx()

        layout(constraints.maxWidth, constraints.maxHeight) {
            indexesWithPlaceables.forEach { (index, placeables) ->
                val item = itemProvider.getItem(index)
                item?.let { placeItem(offsets.toIntOffset(), item, cellPx, paddingPx, placeables) }
            }
        }
    }
}

fun Placeable.PlacementScope.placeItem(
    offset: IntOffset,
    item: CellItem,
    cellSize: Int,
    cellPadding: Int,
    placeables: List<Placeable>
) {
    val xPosition = item.x * (cellSize) - offset.x + cellPadding
    val yPosition = item.y * (cellSize) - offset.y + cellPadding

    placeables.forEach { placeable ->
        placeable.place(
            xPosition,
            yPosition
        )
    }
}


fun Modifier.dragFieldLayout(state: FieldLayoutState): Modifier {
    return this
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                state.onDrag(dragAmount)
            }

//            detectTransformGestures(panZoomLock = true) { centroid, pan, zoom, rotation ->
//                val oldScale = state.elementScale.value
//                val newScale = oldScale * zoom
////
////                if (zoom == 1.0f) {
////                    state.onDrag(IntOffset(pan.x.roundToInt(), pan.y.roundToInt()))
////                    return@detectTransformGestures
////                }
//                val newOffset = (state.offsetState.value + centroid / oldScale) -
//                        (centroid / newScale + pan / oldScale)
//
//                state.onDrag(
//                    Offset(
//                        (-newOffset.x * newScale),
//                        (-newOffset.y * newScale)
//                    )
//                )
//                state.onZoom(newScale)
//            }
        }
}

fun Offset.toIntOffset(): IntOffset {
    return IntOffset(x.roundToInt(), y.roundToInt())
}