package game

import game.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.Duration

// The rules of the game:
// 1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
// 2. Any live cell with two or three live neighbours lives on to the next generation.
// 3. Any live cell with more than three live neighbours dies, as if by overpopulation.
// 4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
class GameOfLife(
    private val scope: CoroutineScope,
    initialGameState: GameState
) {

    val gameStateFlow = MutableStateFlow(initialGameState)

    private var updateGameJob: Job? = null
    private val gameState
        get() = gameStateFlow.value

    fun run() {
        gameStateFlow.tryEmit(gameState.copy(gameStatus = GameStatus.Started))
        updateGameJob = scope.launch {
            while (true) {
                delay(gameState.generationDuration)
                updateGrid()
            }
        }
    }

    fun setGenerationDuration(generationDuration: Duration) {
        gameStateFlow.tryEmit(gameState.copy(generationDuration = generationDuration))
        updateGameJob?.cancel()
        // If the game was already started, we need to make sure that the update loop is restarted after cancelling
        // the old one
        if (gameState.gameStatus == GameStatus.Started) run()
    }

    fun pause() {
        gameStateFlow.tryEmit(gameState.copy(gameStatus = GameStatus.Paused))
        updateGameJob?.cancel()
    }

    fun stop() {
        updateGameJob?.cancel()
    }

    fun resurrectOrKill(cellPosition: Grid.Position) {
        if (cellPosition.isOutOfBounds) return

        val nextGrid = gameState.grid.copy(
            cells = gameState.grid.cells.toMutableList().apply {
                this[cellPosition.y] = gameState.grid.cells[cellPosition.y].toMutableList().apply {
                    this[cellPosition.x] = Cell(
                        isAlive = gameState.grid.cells[cellPosition.y][cellPosition.x].isAlive.not()
                    )
                }
            }
        )

        gameStateFlow.tryEmit(gameState.copy(grid = nextGrid))
    }

    private suspend fun updateGrid() {
        val currentGrid = gameState.grid
        val nextGrid = currentGrid.copy(
            cells = currentGrid.cells.mapIndexed { y, row ->
                row.mapIndexed { x, cell ->
                    val numberOfAliveNeighbours = currentGrid.numberOfLiveNeighbours(Grid.Position(x, y))
                    Cell(isAlive = cellAliveInNextIteration(cell, numberOfAliveNeighbours))
                }
            }
        )

        gameStateFlow.emit(
            gameState.copy(generation = gameState.generation + 1, grid = nextGrid)
        )
    }

    private fun cellAliveInNextIteration(cell: Cell, numberOfAliveNeighbours: Int): Boolean {
        return (cell.isAlive && numberOfAliveNeighbours in (2..3)
                || cell.isAlive.not() && numberOfAliveNeighbours == 3)
    }

    private val Grid.Position.isOutOfBounds
        get() = x >= gameState.grid.size ||
                y >= gameState.grid.size ||
                x < 0 ||
                y < 0
}
