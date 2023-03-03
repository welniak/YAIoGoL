package game.model

data class Grid(val cells: Array<Array<Cell>>) {

    @JvmField
    val size = cells.size

    fun numberOfAliveNeighbours(cellPosition: Position): Int {
        return neighboursOf(cellPosition).count { it.isAlive }
    }

    private fun neighboursOf(cellPosition: Position): List<Cell> {
        val hasTopNeighbours = cellPosition.y > 0
        val hasBottomNeighbours = cellPosition.y < cells.size - 1
        val hasLeftNeighbours = cellPosition.x > 0
        val hasRightNeighbours = cellPosition.x < cells[0].size - 1

        val columnToTheLeft = cellPosition.x - 1
        val columnToTheRight = cellPosition.x + 1
        val rowAbove = cellPosition.y - 1
        val rowBelow = cellPosition.y + 1
        val currentColumn = cellPosition.x
        val currentRow = cellPosition.y

        return mutableListOf<Cell>().apply {
            if (hasLeftNeighbours) {
                if (hasTopNeighbours) add(cells[rowAbove][columnToTheLeft])
                if (hasBottomNeighbours) add(cells[rowBelow][columnToTheLeft])
                add(cells[currentRow][columnToTheLeft])
            }

            if (hasRightNeighbours) {
                if (hasTopNeighbours) add(cells[rowAbove][columnToTheRight])
                if (hasBottomNeighbours) add(cells[rowBelow][columnToTheRight])
                add(cells[currentRow][columnToTheRight])
            }

            if (hasTopNeighbours) add(cells[rowAbove][currentColumn])
            if (hasBottomNeighbours) add(cells[rowBelow][currentColumn])
        }
    }

    override fun equals(other: Any?) = other != null && other is Grid && cells.contentEquals(other.cells)

    override fun hashCode() = cells.contentDeepHashCode()

    data class Position(val x: Int, val y: Int)

    companion object {
        fun random(size: Int): Grid {
            val aliveCellsPercentage = Math.random()
            return Grid(
                cells = Array(size) { _ ->
                    Array(size) { _ ->
                        Cell(isAlive = Math.random() < aliveCellsPercentage)
                    }
                }
            )
        }

        fun empty(size: Int) = Grid(
            cells = Array(size) { _ ->
                Array(size) { _ ->
                    Cell(isAlive = false)
                }
            }
        )
    }
}
