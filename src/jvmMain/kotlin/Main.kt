import androidx.compose.ui.window.application
import ui.composable.Application
import game.GameOfLifeFactoryImpl
import game.model.GameStateFactoryImpl
import game.model.GridFactoryImpl
import game.util.RandomNumberGeneratorImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ui.model.GameUiStateFactoryImpl
import ui.viewmodel.GameOfLifeViewModel

fun main() {
    val gameScope = CoroutineScope(Dispatchers.Default)
    val viewModelScope = CoroutineScope(Dispatchers.Default)
    val randomStateGenerator = RandomNumberGeneratorImpl()
    val gridFactory = GridFactoryImpl(randomStateGenerator)
    val gameStateFactory = GameStateFactoryImpl(gridFactory)
    val gameFactory = GameOfLifeFactoryImpl(gameStateFactory, gameScope)
    val gameUiStateFactory = GameUiStateFactoryImpl(gameStateFactory)
    val viewModel = GameOfLifeViewModel(gameFactory, viewModelScope, gameUiStateFactory)

    application {
        Application(viewModel, onClosed = ::exitApplication)
    }
}
