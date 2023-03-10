package game.model

import game.util.RandomNumberGeneratorImpl
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class GameStateFactoryImplTest {

    @Test
    fun `should create empty game state with right grid size and generation duration`() {
        val gameStateFactory = GameStateFactoryImpl(GridFactoryImpl(RandomNumberGeneratorImpl()))

        val actualEmptyGameState = gameStateFactory.emptyGameState(generationDuration, GridSize)

        with(actualEmptyGameState) {
            assertEquals(0, generation)
            assertEquals(generationDuration, generationDuration)
            assertEquals(GameStatus.Initial, gameStatus)
            assertEquals(GridSize, grid.size)
            assertEquals(0, grid.cells.flatten().count { it.isAlive })
        }
    }

    @Test
    fun `should create random game state with right grid size and generation duration`() {
        val expectedGrid = Grid(arrayOf(arrayOf(Cell(isAlive = true))))
        val gridFactory = object : GridFactory {
            var gridSize = 0
            override fun emptyGrid(size: Int) = throw NotImplementedError()
            override fun randomGrid(size: Int): Grid {
                gridSize = size
                return expectedGrid
            }
        }
        val gameStateFactory = GameStateFactoryImpl(gridFactory)

        val actualRandomGameState = gameStateFactory.randomGameState(generationDuration, GridSize)

        with(actualRandomGameState) {
            assertEquals(0, generation)
            assertEquals(generationDuration, generationDuration)
            assertEquals(GameStatus.Initial, gameStatus)
            assertEquals(GridSize, gridFactory.gridSize)
            assertEquals(expectedGrid, grid)
        }
    }

    private companion object TestData {
        const val GridSize = 42
        val generationDuration = 1.seconds
    }
}
