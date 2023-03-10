package game

import game.model.GameStateFactory
import kotlinx.coroutines.CoroutineScope
import game.model.GridFactoryImpl
import kotlin.time.Duration

class GameOfLifeFactory(
    private val gameStateFactory: GameStateFactory,
    private val gridFactory: GridFactoryImpl,
    private val coroutineScope: CoroutineScope
) {

    fun emptyGame(
        generationDuration: Duration,
        gridSize: Int
    ) = GameOfLife(
        coroutineScope,
        gridFactory,
        gameStateFactory.emptyGameState(generationDuration, gridSize)
    )

    fun randomGame(
        generationDuration: Duration,
        gridSize: Int
    ) = GameOfLife(
        coroutineScope,
        gridFactory,
        gameStateFactory.randomGameState(generationDuration, gridSize)
    )
}
