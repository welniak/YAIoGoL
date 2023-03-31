package game

import fakes.FakeGridFactory
import game.model.*
import game.util.RandomNumberGeneratorImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class GameOfLifeFactoryImplTest {

    private val coroutineScope = TestScope()

    @Test
    fun `should create empty game`() = runTest {
        val gridFactory = GridFactoryImpl(RandomNumberGeneratorImpl())
        val gameStateFactory = GameStateFactoryImpl(gridFactory)
        val gameOfLifeFactory = GameOfLifeFactoryImpl(gameStateFactory, coroutineScope)

        val actualInitialGameState = gameOfLifeFactory.emptyGame(generationDuration, GridSize)
            .gameStateFlow
            .first()

        with(actualInitialGameState) {
            assertEquals(InitialGeneration, generation)
            assertEquals(TestData.generationDuration, generationDuration)
            assertEquals(GameStatus.Initial, gameStatus)
            assertEquals(GridSize, grid.size)
            val numberOfLiveCells = grid.cells.flatten().count { it.isAlive }
            assertEquals(0, numberOfLiveCells)
        }
    }

    @Test
    fun `should create a random game`() = runTest {
        val expectedGrid = Grid(listOf(listOf(Cell(isAlive = true))))
        val gridFactory = FakeGridFactory(randomGrid = expectedGrid)
        val gameStateFactory = GameStateFactoryImpl(gridFactory)
        val gameOfLifeFactory = GameOfLifeFactoryImpl(gameStateFactory, coroutineScope)

        val actualInitialGameState = gameOfLifeFactory.randomGame(generationDuration, GridSize)
            .gameStateFlow
            .first()

        with(actualInitialGameState) {
            assertEquals(InitialGeneration, generation)
            assertEquals(TestData.generationDuration, generationDuration)
            assertEquals(GameStatus.Initial, gameStatus)
            assertEquals(GridSize, gridFactory.gridSize)
            assertEquals(expectedGrid, grid)
        }
    }

    private companion object TestData {
        const val GridSize = 42
        const val InitialGeneration = 0
        val generationDuration = 100.milliseconds
    }
}
