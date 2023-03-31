package game.model

data class Grid(val cells: List<List<Cell>>) {

    @JvmField
    val size = cells.size

    fun numberOfLiveNeighbours(cellPosition: Position): Int {
        return neighboursOf(cellPosition).count { it.isAlive }
    }

    private fun neighboursOf(cellPosition: Position): List<Cell> {
        if (cellPosition.x < 0 || cellPosition.y < 0 || cellPosition.x > cells.size || cellPosition.y > cells.size)
            return emptyList()

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

    data class Position(val x: Int, val y: Int)
}
