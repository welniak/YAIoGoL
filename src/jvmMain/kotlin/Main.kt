import androidx.compose.ui.window.application
import ui.composable.Application
import game.GameOfLifeFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ui.viewmodel.GameOfLifeViewModel

fun main() {
    val gameScope = CoroutineScope(Dispatchers.Default)
    val viewModelScope = CoroutineScope(Dispatchers.Default)
    val gameFactory = GameOfLifeFactory(gameScope)
    val viewModel = GameOfLifeViewModel(gameFactory, viewModelScope)

    application {
        Application(viewModel, onClosed = ::exitApplication)
    }
}
