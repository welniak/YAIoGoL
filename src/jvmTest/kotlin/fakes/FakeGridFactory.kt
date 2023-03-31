package fakes

import game.model.Grid
import game.model.GridFactory

class FakeGridFactory(private val randomGrid: Grid) : GridFactory {

    var gridSize = 0
    override fun emptyGrid(size: Int) = throw NotImplementedError()
    override fun randomGrid(size: Int): Grid {
        gridSize = size
        return randomGrid
    }
}
