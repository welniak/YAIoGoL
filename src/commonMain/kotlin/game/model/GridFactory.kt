package game.model

import game.util.RandomNumberGenerator

interface GridFactory {
    fun emptyGrid(size: Int): Grid
    fun randomGrid(size: Int): Grid
}

class GridFactoryImpl(private val randomNumberGenerator: RandomNumberGenerator) : GridFactory {

    override fun emptyGrid(size: Int) = Grid(
        cells = MutableList(size) { _ ->
            MutableList(size) { _ ->
                Cell(isAlive = false)
            }
        }
    )

    override fun randomGrid(size: Int): Grid {
        val aliveCellsPercentage = randomNumberGenerator.nextDouble()
        return Grid(
            cells = MutableList(size) { _ ->
                MutableList(size) { _ ->
                    Cell(isAlive = randomNumberGenerator.nextDouble() < aliveCellsPercentage)
                }
            }
        )
    }
}
