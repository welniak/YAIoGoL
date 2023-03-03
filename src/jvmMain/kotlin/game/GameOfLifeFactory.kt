package game

import kotlinx.coroutines.CoroutineScope
import game.model.GameState
import kotlin.time.Duration

class GameOfLifeFactory(
    private val coroutineScope: CoroutineScope
) {

    fun emptyGame(
        generationDuration: Duration,
        gridSize: Int
    ) = GameOfLife(
        coroutineScope,
        GameState.emptyGame(generationDuration, gridSize)
    )

    fun randomGame(
        generationDuration: Duration,
        gridSize: Int
    ) = GameOfLife(
        coroutineScope,
        GameState.randomGame(generationDuration, gridSize)
    )
}
