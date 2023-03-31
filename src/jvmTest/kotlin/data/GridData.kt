package data

import game.model.Cell
import game.model.Grid

fun liveCellsGrid(size: Int) = Grid(
    MutableList(size) { _ ->
        MutableList(size) { _ ->
            Cell(isAlive = true)
        }
    }
)

fun deadCellsGrid(size: Int) = Grid(
    MutableList(size) { _ ->
        MutableList(size) { _ ->
            Cell(isAlive = false)
        }
    }
)

fun gridWithOnlyCornersAlive(size: Int): Grid = deadCellsGrid(size)
    .mutable()
    .apply {
        cells[0][0] = Cell(isAlive = true)
        cells[0][size - 1] = Cell(isAlive = true)
        cells[size - 1][0] = Cell(isAlive = true)
        cells[size - 1][size - 1] = Cell(isAlive = true)
    }.immutable()

/**
 * @param blockPosition coordinates of the top-left cell of the block
 */
fun gridWithBlock(size: Int, blockPosition: Grid.Position): Grid = gridWithBlocks(size, blockPosition)

/**
 * @param blockPositions coordinates of the top-left cells of the blocks
 */
fun gridWithBlocks(size: Int, vararg blockPositions: Grid.Position): Grid = deadCellsGrid(size)
    .mutable()
    .apply {
        blockPositions.forEach { blockPosition ->
            cells[blockPosition.x][blockPosition.y] = Cell(isAlive = true)
            cells[blockPosition.x + 1][blockPosition.y] = Cell(isAlive = true)
            cells[blockPosition.x][blockPosition.y + 1] = Cell(isAlive = true)
            cells[blockPosition.x + 1][blockPosition.y + 1] = Cell(isAlive = true)
        }
    }.immutable()

/**
 * @param beehivePosition coordinates of the leftmost cell of the beehive
 */
fun gridWithBeehive(size: Int, beehivePosition: Grid.Position) = gridWithBlocks(size, beehivePosition)

/**
 * @param beehivePositions coordinates of the leftmost cells of the beehives
 */
fun gridWithBeehives(size: Int, vararg beehivePositions: Grid.Position): Grid = deadCellsGrid(size)
    .mutable()
    .apply {
        beehivePositions.forEach { beehivePosition ->
            cells[beehivePosition.x][beehivePosition.y] = Cell(isAlive = true)
            cells[beehivePosition.x + 1][beehivePosition.y + 1] = Cell(isAlive = true)
            cells[beehivePosition.x + 2][beehivePosition.y + 1] = Cell(isAlive = true)
            cells[beehivePosition.x + 3][beehivePosition.y] = Cell(isAlive = true)
            cells[beehivePosition.x + 1][beehivePosition.y - 1] = Cell(isAlive = true)
            cells[beehivePosition.x + 2][beehivePosition.y - 1] = Cell(isAlive = true)
        }
    }.immutable()

/**
 * @param tetrominoPosition coordinates of the top-left cell of the tetromino
 */
fun gridWithTTetromino(size: Int, tetrominoPosition: Grid.Position): Grid = deadCellsGrid(size)
    .mutable()
    .apply {
        cells[tetrominoPosition.x][tetrominoPosition.y] = Cell(isAlive = true)
        cells[tetrominoPosition.x + 1][tetrominoPosition.y] = Cell(isAlive = true)
        cells[tetrominoPosition.x + 2][tetrominoPosition.y] = Cell(isAlive = true)
        cells[tetrominoPosition.x + 1][tetrominoPosition.y + 1] = Cell(isAlive = true)
    }.immutable()

/**
 * @param trafficLightPosition coordinates of the top cell of the leftmost part of the pattern
 */
fun gridWithTrafficLightPhaseOne(size: Int, trafficLightPosition: Grid.Position): Grid = deadCellsGrid(size)
    .mutable()
    .apply {
        cells[trafficLightPosition.x][trafficLightPosition.y] = Cell(isAlive = true)
        cells[trafficLightPosition.x][trafficLightPosition.y + 1] = Cell(isAlive = true)
        cells[trafficLightPosition.x][trafficLightPosition.y + 2] = Cell(isAlive = true)

        cells[trafficLightPosition.x + 2][trafficLightPosition.y - 2] = Cell(isAlive = true)
        cells[trafficLightPosition.x + 3][trafficLightPosition.y - 2] = Cell(isAlive = true)
        cells[trafficLightPosition.x + 4][trafficLightPosition.y - 2] = Cell(isAlive = true)

        cells[trafficLightPosition.x + 2][trafficLightPosition.y + 4] = Cell(isAlive = true)
        cells[trafficLightPosition.x + 3][trafficLightPosition.y + 4] = Cell(isAlive = true)
        cells[trafficLightPosition.x + 4][trafficLightPosition.y + 4] = Cell(isAlive = true)

        cells[trafficLightPosition.x + 6][trafficLightPosition.y] = Cell(isAlive = true)
        cells[trafficLightPosition.x + 6][trafficLightPosition.y + 1] = Cell(isAlive = true)
        cells[trafficLightPosition.x + 6][trafficLightPosition.y + 2] = Cell(isAlive = true)
    }.immutable()

/**
 * @param trafficLightPosition coordinates of the top leftmost cell of the pattern
 */
fun gridWithTrafficLightPhaseTwo(size: Int, trafficLightPosition: Grid.Position): Grid = deadCellsGrid(size)
    .mutable()
    .apply {
        cells[trafficLightPosition.x][trafficLightPosition.y] = Cell(isAlive = true)
        cells[trafficLightPosition.x + 1][trafficLightPosition.y] = Cell(isAlive = true)
        cells[trafficLightPosition.x + 2][trafficLightPosition.y] = Cell(isAlive = true)

        cells[trafficLightPosition.x + 6][trafficLightPosition.y] = Cell(isAlive = true)
        cells[trafficLightPosition.x + 7][trafficLightPosition.y] = Cell(isAlive = true)
        cells[trafficLightPosition.x + 8][trafficLightPosition.y] = Cell(isAlive = true)

        cells[trafficLightPosition.x + 4][trafficLightPosition.y - 2] = Cell(isAlive = true)
        cells[trafficLightPosition.x + 4][trafficLightPosition.y - 3] = Cell(isAlive = true)
        cells[trafficLightPosition.x + 4][trafficLightPosition.y - 4] = Cell(isAlive = true)

        cells[trafficLightPosition.x + 4][trafficLightPosition.y + 2] = Cell(isAlive = true)
        cells[trafficLightPosition.x + 4][trafficLightPosition.y + 3] = Cell(isAlive = true)
        cells[trafficLightPosition.x + 4][trafficLightPosition.y + 4] = Cell(isAlive = true)
    }.immutable()

private class MutableGrid(grid: Grid) {

    val cells: MutableList<MutableList<Cell>> = grid.cells.map { it.toMutableList() }.toMutableList()
}

private fun Grid.mutable() = MutableGrid(this)

private fun MutableGrid.immutable() = Grid(this.cells)
