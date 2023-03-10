package ui.model

import androidx.compose.ui.geometry.Offset
import game.model.DefaultGenerationDuration
import game.model.DefaultGridSize
import game.model.GameStateFactory

class GameUiStateFactory(private val gameStateFactory: GameStateFactory) {

    fun initialState() = GameUiState(
        gameState = gameStateFactory.emptyGameState(DefaultGenerationDuration, DefaultGridSize),
        zoomFactor = 1.0,
        dragOffset = Offset.Zero
    )
}
