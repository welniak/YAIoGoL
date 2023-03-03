package ui.viewmodel

import androidx.compose.ui.geometry.Offset
import ui.composable.CellSize
import game.GameOfLife
import game.GameOfLifeFactory
import ui.model.ClickPosition
import game.model.DefaultGenerationDuration
import game.model.DefaultGridSize
import game.model.GameStatus
import game.model.Grid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ui.model.GameUiState
import ui.model.PivotPointPosition
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class GameOfLifeViewModel(
    private val gameFactory: GameOfLifeFactory,
    private val coroutineScope: CoroutineScope
) {

    val gameUiStateFlow = MutableStateFlow(GameUiState.initial())

    private val gameUiState
        get() = gameUiStateFlow.value

    private val gameState
        get() = gameUiState.gameState

    private var gameUiStateJob: Job? = null
    private var currentGame = gameFactory.emptyGame(DefaultGenerationDuration, DefaultGridSize).apply { observe() }
        set(value) {
            field.stop()
            field = value
            field.observe()
        }

    fun onGenerationDurationChanged(generationDurationMs: Long) {
        currentGame.setGenerationDuration(generationDurationMs.toDuration(DurationUnit.MILLISECONDS))
    }

    fun onStartPauseToggled() {
        if (gameState.gameStatus == GameStatus.Started) {
            currentGame.pause()
        } else {
            currentGame.run()
        }
    }

    fun onStoppedClicked() {
        currentGame = gameFactory.emptyGame(
            generationDuration = gameState.generationDuration,
            gridSize = gameState.grid.size
        )
    }

    fun onRandomizeClicked() {
        currentGame = gameFactory.randomGame(
            generationDuration = gameState.generationDuration,
            gridSize = gameState.grid.size
        )
    }

    fun onClearGridClicked() {
        currentGame = gameFactory.emptyGame(
            generationDuration = gameState.generationDuration,
            gridSize = gameState.grid.size
        )
    }

    fun onResetViewClicked() {
        gameUiStateFlow.tryEmit(gameUiState.copy(zoomFactor = 1.0, dragOffset = Offset.Zero))
    }

    fun onGridSizeChanged(gridSize: Int) {
        currentGame = gameFactory.emptyGame(
            generationDuration = gameState.generationDuration,
            gridSize = gridSize
        )
    }

    fun onScrolled(scrollDelta: Float) {
        val newZoomFactor = if (scrollDelta > 0f) {
            (gameUiState.zoomFactor + 0.1).coerceAtMost(MaxZoomFactor)
        } else {
            (gameUiState.zoomFactor - 0.1).coerceAtLeast(MinZoomFactor)
        }
        gameUiStateFlow.tryEmit(gameUiState.copy(zoomFactor = newZoomFactor))
    }

    fun onDragged(dragAmount: Offset) {
        // The dragging distance is inversely proportional to the current zoom
        val scaledDragAmount = dragAmount.div(gameUiState.zoomFactor.toFloat())
        val newDragOffset = gameUiState.dragOffset.copy(
            x = (gameUiState.dragOffset.x + scaledDragAmount.x),
            y = (gameUiState.dragOffset.y + scaledDragAmount.y)
        )
        gameUiStateFlow.tryEmit(gameUiState.copy(dragOffset = newDragOffset))
    }

    fun onClicked(clickPosition: ClickPosition, pivotPointPosition: PivotPointPosition) {
        if (gameState.gameStatus != GameStatus.Initial) return
        // Transform the click coordinates to match the current UI transformation
        val shiftedClick = clickPosition.offset
            .minus(gameUiState.dragOffset)
            .div(gameUiState.zoomFactor.toFloat())
        val transformedClick = shiftedClick
            .plus(pivotPointPosition.offset)
            .times(gameUiState.zoomFactor.toFloat())
            .minus(pivotPointPosition.offset)

        val (x, y) = (transformedClick / gameUiState.zoomFactor.toFloat() / CellSize).let {
            Pair(it.x.toInt(), it.y.toInt())
        }

        currentGame.resurrectOrKill(cellPosition = Grid.Position(x, y))
    }

    private fun GameOfLife.observe() {
        gameUiStateJob?.cancel()
        gameUiStateJob = gameStateFlow
            .map { newGameState -> gameUiState.copy(gameState = newGameState) }
            .onEach { gameUiStateFlow.tryEmit(it) }
            .launchIn(coroutineScope)
    }
}

private const val MinZoomFactor = 0.2
private const val MaxZoomFactor = 5.0
