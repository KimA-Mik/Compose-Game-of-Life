package ru.kima.gameoflife.presentation.screens.gameoflife.layout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.kima.gameoflife.presentation.screens.gameoflife.model.CellItem
import ru.kima.gameoflife.presentation.util.conditional
import kotlin.math.roundToInt

val CELL_SIZE = 48.dp
const val BORDER_RATIO = 0.01f

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FieldLayout(
    items: List<CellItem>,
    state: FieldLayoutState,
    modifier: Modifier = Modifier,
    cellClickable: Boolean,
    cellOnClick: CellItemOnClick,
    cell: @Composable (cellState: Int) -> Unit
) {

    val cellScale by state.elementScale.collectAsStateWithLifecycle()
    val offset by state.offsetState.collectAsStateWithLifecycle()

    val itemProvider = rememberItemProvider(items = items) {
        val cellState by it.state.collectAsStateWithLifecycle()
        Box(modifier = Modifier
            .size(cellScale * CELL_SIZE)
            .border(
                width = CELL_SIZE * cellScale * BORDER_RATIO,
                color = MaterialTheme.colorScheme.primary
            )
            .conditional(cellClickable) {
                clickable { cellOnClick(it) }
            }) {
            cell(cellState)
        }
    }

    val intOffset = offset.toIntOffset()
    LazyLayout(
        itemProvider = { itemProvider },
        modifier = modifier.dragFieldLayout(state)
    ) { constraints ->
        val totalCellSize = (cellScale * CELL_SIZE)
        val indexes = itemProvider.getIndexesInConstrains(
            constraints = constraints,
            spaceTaken = totalCellSize.roundToPx(),
            offset = intOffset
        )
        val indexesWithPlaceables = indexes.associateWith {
            measure(it, Constraints())
        }
        val cellPx = (CELL_SIZE * cellScale).roundToPx()
        layout(constraints.maxWidth, constraints.maxHeight) {
            indexesWithPlaceables.forEach { (index, placeables) ->
                val item = itemProvider.getItem(index)
                item?.let { placeItem(intOffset, item, cellPx, placeables) }
            }
        }
    }
}

fun Placeable.PlacementScope.placeItem(
    offset: IntOffset,
    item: CellItem,
    cellSize: Int,
    placeables: List<Placeable>
) {
    val xPosition = item.x * (cellSize) - offset.x
    val yPosition = item.y * (cellSize) - offset.y

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
//            detectDragGestures { change, dragAmount ->
//                change.consume()
//                state.onDrag(dragAmount)
//            }

            detectTransformGestures(panZoomLock = true) { centroid, pan, zoom, rotation ->
                val oldScale = state.elementScale.value
                val newScale = oldScale * zoom
//
                if (zoom == 1.0f) {
                    state.onDrag(pan)
                    return@detectTransformGestures
                }
                val newOffset = (state.offsetState.value + centroid / oldScale) -
                        (centroid / newScale + pan / oldScale)

                state.setOffset(
                    Offset(
                        (-newOffset.x * newScale),
                        (-newOffset.y * newScale)
                    )
                )
                state.onZoom(newScale)
            }
        }
}

fun Offset.toIntOffset(): IntOffset {
    return IntOffset(x.roundToInt(), y.roundToInt())
}