package ui.model

import androidx.compose.ui.geometry.Offset
import game.model.*
import game.util.RandomNumberGeneratorImpl
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GameUiStateFactoryImplTest {

    @Test
    fun `should create the initial ui state`() {
        val gameStateFactory = GameStateFactoryImpl(GridFactoryImpl(RandomNumberGeneratorImpl()))
        val gameUiStateFactory = GameUiStateFactoryImpl(gameStateFactory)

        val actualInitialUiState = gameUiStateFactory.initialState()

        with(actualInitialUiState) {
            with(gameState) {
                assertEquals(InitialGeneration, generation)
                assertEquals(DefaultGenerationDuration, generationDuration)
                assertEquals(GameStatus.Initial, gameStatus)
                assertEquals(DefaultGridSize, grid.size)
                val numberOfLiveCells = grid.cells.flatten().count { it.isAlive }
                assertEquals(0, numberOfLiveCells)
            }
            assertEquals(InitialZoomFactor, zoomFactor)
            assertEquals(InitialDragOffset, dragOffset)
        }
    }

    private companion object TestData {
        const val InitialZoomFactor = 1.0
        val InitialDragOffset = Offset.Zero
        const val InitialGeneration = 0
    }
}
