package ui.composable

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.dp
import ui.model.ClickPosition
import ui.model.GameUiState
import ui.model.PivotPointPosition

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
@Preview
fun Grid(
    gameUiState: GameUiState,
    onScrolled: (Float) -> Unit,
    onDragged: (Offset) -> Unit,
    onClicked: (ClickPosition, PivotPointPosition) -> Unit
) {
    val gameGrid = gameUiState.gameState.grid
    var middlePoint = remember { Offset.Zero }
    val modifier = Modifier
        .fillMaxSize()
        .width(800.dp)
        .background(Color.White)
        .scrollable(rememberScrollableState { scrollDelta ->
            onScrolled(scrollDelta)
            scrollDelta
        }, orientation = Orientation.Vertical, flingBehavior = ScrollableDefaults.flingBehavior())
        .pointerInput(Unit) {
            detectDragGestures(matcher = PointerMatcher.mouse(PointerButton.Secondary)) { dragAmount ->
                onDragged(dragAmount)
            }
        }
        .onPointerEvent(PointerEventType.Press) { pointerEvent ->
            if (pointerEvent.buttons.isPrimaryPressed) {
                onClicked(
                    ClickPosition(pointerEvent.changes.first().position),
                    PivotPointPosition(middlePoint)
                )
            }
        }
        .clipToBounds()

    Canvas(modifier = modifier) {
        withTransform({
            middlePoint = center.minus(gameUiState.dragOffset)
            translate(left = gameUiState.dragOffset.x, top = gameUiState.dragOffset.y)
            scale(
                scaleX = gameUiState.zoomFactor.toFloat(),
                scaleY = gameUiState.zoomFactor.toFloat(),
                pivot = middlePoint
            )
        }) {
            repeat(gameGrid.size) { rowIndex ->
                repeat(gameGrid.size) { columnIndex ->
                    val cellAlive = gameGrid.cells[rowIndex][columnIndex].isAlive
                    drawRect(
                        topLeft = Offset(columnIndex * CellSize, rowIndex * CellSize),
                        brush = SolidColor(if (cellAlive) Color.Black else Color.Gray),
                        size = Size(CellSize, CellSize),
                        style = if (cellAlive) Fill else Stroke()
                    )
                }
            }
        }
    }
}

const val CellSize = 15f
