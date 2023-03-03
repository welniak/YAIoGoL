package ui.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import ui.viewmodel.GameOfLifeViewModel

@Composable
fun Application(
    viewModel: GameOfLifeViewModel,
    onClosed: () -> Unit
) {
    val gameUiState by viewModel.gameUiStateFlow.collectAsState()

    Window(
        onCloseRequest = onClosed,
        title = "Generation ${gameUiState.gameState.generation}",
        state = rememberWindowState(width = 1200.dp, height = 800.dp)
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
                    onGridSizeChanged =  viewModel::onGridSizeChanged
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
}
