package game.model

import kotlin.time.Duration

interface GameStateFactory {
    fun emptyGameState(
        generationDuration: Duration,
        gridSize: Int
    ): GameState

    fun randomGameState(
        generationDuration: Duration,
        gridSize: Int
    ): GameState
}

class GameStateFactoryImpl(private val gridFactory: GridFactory) : GameStateFactory {

    override fun emptyGameState(
        generationDuration: Duration,
        gridSize: Int
    ) = GameState(
        generation = 0,
        generationDuration = generationDuration,
        gameStatus = GameStatus.Initial,
        grid = gridFactory.emptyGrid(gridSize)
    )

    override fun randomGameState(
        generationDuration: Duration,
        gridSize: Int
    ) = GameState(
        generation = 0,
        generationDuration = generationDuration,
        gameStatus = GameStatus.Initial,
        grid = gridFactory.randomGrid(gridSize)
    )
}
