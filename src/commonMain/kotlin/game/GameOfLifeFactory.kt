package game

import game.model.GameStateFactory
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration

interface GameOfLifeFactory {
    fun emptyGame(
        generationDuration: Duration,
        gridSize: Int
    ): GameOfLife

    fun randomGame(
        generationDuration: Duration,
        gridSize: Int
    ): GameOfLife
}

class GameOfLifeFactoryImpl(
    private val gameStateFactory: GameStateFactory,
    private val coroutineScope: CoroutineScope
) : GameOfLifeFactory {

    override fun emptyGame(
        generationDuration: Duration,
        gridSize: Int
    ) = GameOfLife(
        coroutineScope,
        gameStateFactory.emptyGameState(generationDuration, gridSize)
    )

    override fun randomGame(
        generationDuration: Duration,
        gridSize: Int
    ) = GameOfLife(
        coroutineScope,
        gameStateFactory.randomGameState(generationDuration, gridSize)
    )
}
