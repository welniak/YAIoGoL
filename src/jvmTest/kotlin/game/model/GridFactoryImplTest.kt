package game.model

import game.util.RandomNumberGenerator
import game.util.RandomNumberGeneratorImpl
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GridFactoryImplTest {

    @Test
    fun `should create an empty grid of right size`() {
        val gridSize = 42
        val gridFactory = GridFactoryImpl(RandomNumberGeneratorImpl())

        val emptyGrid = gridFactory.emptyGrid(gridSize)

        assertEquals(gridSize, emptyGrid.size)
        val numberOfLiveCells = emptyGrid.cells.flatten().count { it.isAlive }
        assertEquals(0, numberOfLiveCells)
    }

    @Test
    fun `should create a random grid of right size`() {
        val gridSize = 2
        // Twice the probability will be over the threshold (first probability) and twice it will be below
        val probabilities = listOf(0.5, 0.1, 0.7, 0.8, 0.2)
        val randomNumberGenerator = object : RandomNumberGenerator {
            private val numbersQueue = ArrayDeque(probabilities)
            override fun nextDouble() = numbersQueue.removeFirst()
        }
        val gridFactory = GridFactoryImpl(randomNumberGenerator)
        val expectedNumberOfLiveCells = 2

        val emptyGrid = gridFactory.randomGrid(gridSize)

        assertEquals(gridSize, emptyGrid.size)
        val numberOfLiveCells = emptyGrid.cells.flatten().count { it.isAlive }
        assertEquals(expectedNumberOfLiveCells, numberOfLiveCells)
    }
}
