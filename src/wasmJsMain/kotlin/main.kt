
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import game.GameOfLifeFactoryImpl
import game.model.GameStateFactoryImpl
import game.model.GridFactoryImpl
import game.util.RandomNumberGeneratorImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ui.composable.Application
import ui.model.GameUiStateFactoryImpl
import ui.viewmodel.GameOfLifeViewModel

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val gameScope = CoroutineScope(Dispatchers.Default)
    val viewModelScope = CoroutineScope(Dispatchers.Default)
    val randomStateGenerator = RandomNumberGeneratorImpl()
    val gridFactory = GridFactoryImpl(randomStateGenerator)
    val gameStateFactory = GameStateFactoryImpl(gridFactory)
    val gameFactory = GameOfLifeFactoryImpl(gameStateFactory, gameScope)
    val gameUiStateFactory = GameUiStateFactoryImpl(gameStateFactory)
    val viewModel = GameOfLifeViewModel(gameFactory, viewModelScope, gameUiStateFactory)

    CanvasBasedWindow("YAIoGol") {
        val gameUiState by viewModel.gameUiStateFlow.collectAsState()
        Application(viewModel, gameUiState)
    }
}
