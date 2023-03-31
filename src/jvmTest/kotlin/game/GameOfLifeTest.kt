package game

import app.cash.turbine.test
import data.*
import game.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GameOfLifeTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val gameOfLife = gameOfLifeWith(InitialState)

    @Test
    fun `should emit the initial state and nothing else when the game is not run`() = runTest {
        gameOfLife.gameStateFlow.test {
            val actualInitialState = awaitItem()

            assertEquals(InitialState, actualInitialState)
        }
    }

    @ParameterizedTest
    @MethodSource("gridEvolutionsInputs")
    fun `should evolve the grid and update the generation count`(
        startGrid: Grid,
        numberOfGenerationsToSkip: Int,
        endGrid: Grid
    ) = runTest(dispatcher) {
        val initialState = InitialState.copy(grid = startGrid)
        val gameOfLife = gameOfLifeWith(initialState)
        val expectedFirstStartedState = initialState.copy(gameStatus = GameStatus.Started)
        val expectedFinalState = expectedFirstStartedState.copy(
            generation = numberOfGenerationsToSkip,
            gameStatus = GameStatus.Paused,
            grid = endGrid
        )

        gameOfLife.gameStateFlow.test {
            // Initial state
            skipItems(1)

            gameOfLife.run()

            val firstStartedState = awaitItem()
            assertEquals(expectedFirstStartedState, firstStartedState)

            skipItems(numberOfGenerationsToSkip)
            gameOfLife.pause()

            val finalState = awaitItem()
            assertEquals(expectedFinalState, finalState)
        }
    }

    @Test
    fun `should change the generation duration and emit a new updated state`() = runTest {
        val expectedState = InitialState.copy(generationDuration = UpdatedGenerationDuration)

        gameOfLife.gameStateFlow.test {
            // Initial state
            skipItems(1)

            gameOfLife.setGenerationDuration(UpdatedGenerationDuration)

            val actualState = awaitItem()
            assertEquals(expectedState, actualState)
        }
    }

    @Test
    fun `should emit a paused state when game paused`() = runTest {
        val expectedState = InitialState.copy(gameStatus = GameStatus.Paused)

        gameOfLife.gameStateFlow.test {
            // Initial state
            skipItems(1)

            gameOfLife.run()
            // Started state
            skipItems(1)
            gameOfLife.pause()

            val actualState = awaitItem()
            assertEquals(expectedState, actualState)
        }
    }

    @Test
    fun `should stop emitting when game is stopped`() = runTest {
        gameOfLife.gameStateFlow.test {
            // Initial state
            skipItems(1)

            gameOfLife.run()
            // Started state
            skipItems(1)
            gameOfLife.stop()

            expectNoEvents()
        }
    }

    @Test
    fun `should not emit anything when trying to resurrect or kill a cell out of bounds`() = runTest {
        gameOfLife.gameStateFlow.test {
            // Initial state
            skipItems(1)

            gameOfLife.resurrectOrKill(Grid.Position(-1, 0))
            gameOfLife.resurrectOrKill(Grid.Position(0, -1))
            gameOfLife.resurrectOrKill(Grid.Position(InitialState.grid.size, -1))
            gameOfLife.resurrectOrKill(Grid.Position(0, InitialState.grid.size))

            expectNoEvents()
        }
    }

    @Test
    fun `should kill cell if already alive and not increment generation when resurrecting or killing`() = runTest {
        val gameOfLife = gameOfLifeWith(InitialState.copy(grid = liveCellsGrid(1)))
        val expectedState = InitialState.copy(grid = deadCellsGrid(1))

        gameOfLife.gameStateFlow.test {
            // Initial state
            skipItems(1)

            gameOfLife.resurrectOrKill(Grid.Position(0, 0))

            val actualState = awaitItem()
            assertEquals(expectedState, actualState)
        }
    }

    @Test
    fun `should resurrect cell if dead and not increment generation when resurrecting or killing`() = runTest {
        val gameOfLife = gameOfLifeWith(InitialState.copy(grid = deadCellsGrid(1)))
        val expectedState = InitialState.copy(grid = liveCellsGrid(1))

        gameOfLife.gameStateFlow.test {
            // Initial state
            skipItems(1)

            gameOfLife.resurrectOrKill(Grid.Position(0, 0))

            val actualState = awaitItem()
            assertEquals(expectedState, actualState)
        }
    }

    private fun gameOfLifeWith(initialState: GameState) = GameOfLife(
        scope = CoroutineScope(dispatcher),
        initialGameState = initialState
    )

    private companion object TestData {
        val InitialState = GameState(
            generation = 0,
            generationDuration = DefaultGenerationDuration,
            gameStatus = GameStatus.Initial,
            grid = Grid(listOf(listOf(Cell(isAlive = true))))
        )
        val UpdatedGenerationDuration = DefaultGenerationDuration * 2

        @JvmStatic
        private fun gridEvolutionsInputs() = Stream.of(
            // Empty grids remain empty
            Arguments.of(deadCellsGrid(5), 1, deadCellsGrid(5)),
            Arguments.of(deadCellsGrid(5), 10, deadCellsGrid(5)),
            Arguments.of(deadCellsGrid(5), 100, deadCellsGrid(5)),
            Arguments.of(deadCellsGrid(500), 1, deadCellsGrid(500)),
            Arguments.of(deadCellsGrid(500), 10, deadCellsGrid(500)),
            Arguments.of(deadCellsGrid(500), 100, deadCellsGrid(500)),

            // Full grids lead to only corners being alive after 1 generation
            Arguments.of(liveCellsGrid(5), 1, gridWithOnlyCornersAlive(5)),
            Arguments.of(liveCellsGrid(50), 1, gridWithOnlyCornersAlive(50)),
            Arguments.of(liveCellsGrid(500), 1, gridWithOnlyCornersAlive(500)),

            // Full grids die out from overpopulation after 2 generations
            Arguments.of(liveCellsGrid(5), 2, deadCellsGrid(5)),
            Arguments.of(liveCellsGrid(5), 10, deadCellsGrid(5)),
            Arguments.of(liveCellsGrid(5), 100, deadCellsGrid(5)),
            Arguments.of(liveCellsGrid(500), 2, deadCellsGrid(500)),
            Arguments.of(liveCellsGrid(500), 10, deadCellsGrid(500)),
            Arguments.of(liveCellsGrid(500), 100, deadCellsGrid(500)),

            // Single block remains unchanged
            Arguments.of(
                gridWithBlock(size = 5, blockPosition = Grid.Position(0, 0)),
                1,
                gridWithBlock(size = 5, blockPosition = Grid.Position(0, 0))
            ),
            Arguments.of(
                gridWithBlock(size = 50, blockPosition = Grid.Position(0, 0)),
                1,
                gridWithBlock(size = 50, blockPosition = Grid.Position(0, 0))
            ),
            Arguments.of(
                gridWithBlock(size = 500, blockPosition = Grid.Position(0, 0)),
                1,
                gridWithBlock(size = 500, blockPosition = Grid.Position(0, 0))
            ),
            Arguments.of(
                gridWithBlock(size = 5, blockPosition = Grid.Position(0, 0)),
                100,
                gridWithBlock(size = 5, blockPosition = Grid.Position(0, 0))
            ),
            Arguments.of(
                gridWithBlock(size = 50, blockPosition = Grid.Position(0, 0)),
                100,
                gridWithBlock(size = 50, blockPosition = Grid.Position(0, 0))
            ),
            Arguments.of(
                gridWithBlock(size = 500, blockPosition = Grid.Position(0, 0)),
                100,
                gridWithBlock(size = 500, blockPosition = Grid.Position(0, 0))
            ),

            // Multiple blocks that are not neighbouring remain unchanged
            Arguments.of(
                gridWithBlocks(size = 50, Grid.Position(0, 0), Grid.Position(12, 12)),
                1,
                gridWithBlocks(size = 50, Grid.Position(0, 0), Grid.Position(12, 12)),
            ),
            Arguments.of(
                gridWithBlocks(size = 500, Grid.Position(0, 0), Grid.Position(120, 120)),
                1,
                gridWithBlocks(size = 500, Grid.Position(0, 0), Grid.Position(120, 120)),
            ),
            Arguments.of(
                gridWithBlocks(size = 50, Grid.Position(0, 0), Grid.Position(12, 12)),
                100,
                gridWithBlocks(size = 50, Grid.Position(0, 0), Grid.Position(12, 12)),
            ),
            Arguments.of(
                gridWithBlocks(size = 500, Grid.Position(0, 0), Grid.Position(120, 120)),
                100,
                gridWithBlocks(size = 500, Grid.Position(0, 0), Grid.Position(120, 120)),
            ),

            // Single beehive remains unchanged
            Arguments.of(
                gridWithBeehive(size = 5, beehivePosition = Grid.Position(0, 1)),
                1,
                gridWithBeehive(size = 5, beehivePosition = Grid.Position(0, 1))
            ),
            Arguments.of(
                gridWithBeehive(size = 50, beehivePosition = Grid.Position(0, 1)),
                1,
                gridWithBeehive(size = 50, beehivePosition = Grid.Position(0, 1))
            ),
            Arguments.of(
                gridWithBeehive(size = 500, beehivePosition = Grid.Position(0, 1)),
                1,
                gridWithBeehive(size = 500, beehivePosition = Grid.Position(0, 1))
            ),
            Arguments.of(
                gridWithBeehive(size = 5, beehivePosition = Grid.Position(0, 1)),
                100,
                gridWithBeehive(size = 5, beehivePosition = Grid.Position(0, 1))
            ),
            Arguments.of(
                gridWithBeehive(size = 50, beehivePosition = Grid.Position(0, 1)),
                100,
                gridWithBeehive(size = 50, beehivePosition = Grid.Position(0, 1))
            ),
            Arguments.of(
                gridWithBeehive(size = 500, beehivePosition = Grid.Position(0, 1)),
                100,
                gridWithBeehive(size = 500, beehivePosition = Grid.Position(0, 1))
            ),

            // Multiple beehives that are not neighbouring remain unchanged
            Arguments.of(
                gridWithBeehives(size = 50, Grid.Position(0, 1), Grid.Position(12, 12)),
                1,
                gridWithBeehives(size = 50, Grid.Position(0, 1), Grid.Position(12, 12)),
            ),
            Arguments.of(
                gridWithBeehives(size = 500, Grid.Position(0, 1), Grid.Position(120, 120)),
                1,
                gridWithBeehives(size = 500, Grid.Position(0, 1), Grid.Position(120, 120)),
            ),
            Arguments.of(
                gridWithBeehives(size = 50, Grid.Position(0, 1), Grid.Position(12, 12)),
                100,
                gridWithBeehives(size = 50, Grid.Position(0, 1), Grid.Position(12, 12)),
            ),
            Arguments.of(
                gridWithBeehives(size = 500, Grid.Position(0, 1), Grid.Position(120, 120)),
                100,
                gridWithBeehives(size = 500, Grid.Position(0, 1), Grid.Position(120, 120)),
            ),

            // T Tertomino evolutions
            Arguments.of(
                gridWithTTetromino(size = 100, tetrominoPosition = Grid.Position(50, 50)),
                9,
                gridWithTrafficLightPhaseOne(size = 100, trafficLightPosition = Grid.Position(48, 49))
            ),
            Arguments.of(
                gridWithTTetromino(size = 100, tetrominoPosition = Grid.Position(50, 50)),
                10,
                gridWithTrafficLightPhaseTwo(size = 100, trafficLightPosition = Grid.Position(47, 50))
            ),
            Arguments.of(
                gridWithTTetromino(size = 100, tetrominoPosition = Grid.Position(50, 50)),
                377,
                gridWithTrafficLightPhaseOne(size = 100, trafficLightPosition = Grid.Position(48, 49))
            ),
            Arguments.of(
                gridWithTTetromino(size = 100, tetrominoPosition = Grid.Position(50, 50)),
                1678,
                gridWithTrafficLightPhaseTwo(size = 100, trafficLightPosition = Grid.Position(47, 50))
            ),
        )

    }
}
