package game.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GridTest {

    @Test
    fun `should expose correct grid size`() {
        val size = 42
        val grid = liveCellsGrid(size)

        assertEquals(size, grid.size)
    }

    @ParameterizedTest
    @MethodSource("provideNumberOfNeighboursInputs")
    fun `should return correct number of neighbours`(
        grid: Grid,
        position: Grid.Position,
        numberOfLiveNeighbours: Int
    ) {
        assertEquals(numberOfLiveNeighbours, grid.numberOfLiveNeighbours(position))
    }

    private fun liveCellsGrid(size: Int) = Grid(
        Array(size) { _ ->
            Array(size) { _ ->
                Cell(isAlive = true)
            }
        }
    )

    private fun deadCellsGrid(size: Int) = Grid(
        Array(size) { _ ->
            Array(size) { _ ->
                Cell(isAlive = false)
            }
        }
    )

    private fun provideNumberOfNeighboursInputs(): Stream<Arguments> {
        return Stream.of(
            // Out of bounds -> no live neighbours
            Arguments.of(liveCellsGrid(3), Grid.Position(10, 10), 0),
            Arguments.of(liveCellsGrid(3), Grid.Position(0, 10), 0),
            Arguments.of(liveCellsGrid(3), Grid.Position(-1, 0), 0),
            Arguments.of(liveCellsGrid(3), Grid.Position(0, -1), 0),

            // 3x3 grid with all cells alive
            Arguments.of(liveCellsGrid(3), Grid.Position(0, 0), 3),
            Arguments.of(liveCellsGrid(3), Grid.Position(0, 1), 5),
            Arguments.of(liveCellsGrid(3), Grid.Position(0, 2), 3),
            Arguments.of(liveCellsGrid(3), Grid.Position(1, 0), 5),
            Arguments.of(liveCellsGrid(3), Grid.Position(1, 1), 8),
            Arguments.of(liveCellsGrid(3), Grid.Position(1, 2), 5),
            Arguments.of(liveCellsGrid(3), Grid.Position(2, 0), 3),
            Arguments.of(liveCellsGrid(3), Grid.Position(2, 1), 5),
            Arguments.of(liveCellsGrid(3), Grid.Position(2, 2), 3),

            // 100x100 grid with all cells alive
            Arguments.of(liveCellsGrid(100), Grid.Position(0, 0), 3),
            Arguments.of(liveCellsGrid(100), Grid.Position(0, 1), 5),
            Arguments.of(liveCellsGrid(100), Grid.Position(0, 37), 5),
            Arguments.of(liveCellsGrid(100), Grid.Position(0, 99), 3),
            Arguments.of(liveCellsGrid(100), Grid.Position(1, 0), 5),
            Arguments.of(liveCellsGrid(100), Grid.Position(1, 1), 8),
            Arguments.of(liveCellsGrid(100), Grid.Position(1, 37), 8),
            Arguments.of(liveCellsGrid(100), Grid.Position(1, 99), 5),
            Arguments.of(liveCellsGrid(100), Grid.Position(37, 0), 5),
            Arguments.of(liveCellsGrid(100), Grid.Position(37, 1), 8),
            Arguments.of(liveCellsGrid(100), Grid.Position(37, 37), 8),
            Arguments.of(liveCellsGrid(100), Grid.Position(37, 99), 5),
            Arguments.of(liveCellsGrid(100), Grid.Position(0, 0), 3),
            Arguments.of(liveCellsGrid(100), Grid.Position(0, 1), 5),
            Arguments.of(liveCellsGrid(100), Grid.Position(0, 37), 5),
            Arguments.of(liveCellsGrid(100), Grid.Position(0, 99), 3),

            // 3x3 grid with all cells dead
            Arguments.of(deadCellsGrid(3), Grid.Position(0, 0), 0),
            Arguments.of(deadCellsGrid(3), Grid.Position(0, 1), 0),
            Arguments.of(deadCellsGrid(3), Grid.Position(0, 2), 0),
            Arguments.of(deadCellsGrid(3), Grid.Position(1, 0), 0),
            Arguments.of(deadCellsGrid(3), Grid.Position(1, 1), 0),
            Arguments.of(deadCellsGrid(3), Grid.Position(1, 2), 0),
            Arguments.of(deadCellsGrid(3), Grid.Position(2, 0), 0),
            Arguments.of(deadCellsGrid(3), Grid.Position(2, 1), 0),
            Arguments.of(deadCellsGrid(3), Grid.Position(2, 2), 0),

            // 100x100 grid with all cells dead
            Arguments.of(deadCellsGrid(100), Grid.Position(0, 0), 0),
            Arguments.of(deadCellsGrid(100), Grid.Position(0, 1), 0),
            Arguments.of(deadCellsGrid(100), Grid.Position(0, 37), 0),
            Arguments.of(deadCellsGrid(100), Grid.Position(0, 99), 0),
            Arguments.of(deadCellsGrid(100), Grid.Position(1, 0), 0),
            Arguments.of(deadCellsGrid(100), Grid.Position(1, 1), 0),
            Arguments.of(deadCellsGrid(100), Grid.Position(1, 37), 0),
            Arguments.of(deadCellsGrid(100), Grid.Position(1, 99), 0),
            Arguments.of(deadCellsGrid(100), Grid.Position(37, 0), 0),
            Arguments.of(deadCellsGrid(100), Grid.Position(37, 1), 0),
            Arguments.of(deadCellsGrid(100), Grid.Position(37, 37), 0),
            Arguments.of(deadCellsGrid(100), Grid.Position(37, 99), 0),
            Arguments.of(deadCellsGrid(100), Grid.Position(0, 0), 0),
            Arguments.of(deadCellsGrid(100), Grid.Position(0, 1), 0),
            Arguments.of(deadCellsGrid(100), Grid.Position(0, 37), 0),
            Arguments.of(deadCellsGrid(100), Grid.Position(0, 99), 0),
        )
    }
}
