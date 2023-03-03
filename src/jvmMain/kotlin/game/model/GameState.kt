package game.model

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

data class GameState(
    val generation: Int,
    val generationDuration: Duration,
    val gameStatus: GameStatus,
    val grid: Grid
) {
    companion object {
        fun emptyGame(
            generationDuration: Duration,
            gridSize: Int
        ) = GameState(
            generation = 0,
            generationDuration = generationDuration,
            gameStatus = GameStatus.Initial,
            grid = Grid.empty(gridSize)
        )

        fun randomGame(
            generationDuration: Duration,
            gridSize: Int
        ) = GameState(
            generation = 0,
            generationDuration = generationDuration,
            gameStatus = GameStatus.Initial,
            grid = Grid.random(gridSize)
        )
    }
}

val DefaultGenerationDuration = 1.seconds
val MinGenerationDuration = 30.milliseconds
val MaxGenerationDuration = 3.seconds

const val DefaultGridSize = 200
const val MinGridSize = 10
const val MaxGridSize = 300
