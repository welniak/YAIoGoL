package ui.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import ui.model.GameUiState
import ui.viewmodel.GameOfLifeViewModel

@Composable
fun Application(
    viewModel: GameOfLifeViewModel,
    gameUiState: GameUiState
) {
    MaterialTheme {
        Row {
            SettingsPanel(
                gameUiState.gameState.generationDuration,
                gameUiState.gameState.grid.size,
                gameUiState.gameState.gameStatus,
                onGenerationDurationChanged = viewModel::onGenerationDurationChanged,
                onStartPauseButtonClicked = viewModel::onStartPauseToggled,
                onStopButtonClicked = viewModel::onStoppedClicked,
                onRandomizeButtonClicked = viewModel::onRandomizeClicked,
                onClearGridButtonClicked = viewModel::onClearGridClicked,
                onResetViewButtonClicked = viewModel::onResetViewClicked,
                onGridSizeChanged = viewModel::onGridSizeChanged
            )
            Grid(
                gameUiState,
                onScrolled = viewModel::onScrolled,
                onDragged = viewModel::onDragged,
                onClicked = viewModel::onClicked
            )
        }
    }
}
