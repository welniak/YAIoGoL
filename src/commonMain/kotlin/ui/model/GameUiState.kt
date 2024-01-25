package ui.model

import androidx.compose.ui.geometry.Offset
import game.model.GameState

data class GameUiState(
    val gameState: GameState,
    val zoomFactor: Double,
    val dragOffset: Offset
)
