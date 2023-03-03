package ui.model

import androidx.compose.ui.geometry.Offset
import game.model.DefaultGenerationDuration
import game.model.DefaultGridSize
import game.model.GameState

data class GameUiState(
    val gameState: GameState,
    val zoomFactor: Double,
    val dragOffset: Offset
) {

    companion object {
        fun initial() = GameUiState(
            gameState = GameState.emptyGame(DefaultGenerationDuration, DefaultGridSize),
            zoomFactor = 1.0,
            dragOffset = Offset.Zero
        )
    }
}
