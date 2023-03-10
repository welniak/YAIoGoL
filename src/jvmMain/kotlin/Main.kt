import androidx.compose.ui.window.application
import ui.composable.Application
import game.GameOfLifeFactory
import game.model.GameStateFactoryImpl
import game.model.GridFactoryImpl
import game.util.RandomNumberGeneratorImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ui.model.GameUiStateFactory
import ui.viewmodel.GameOfLifeViewModel

fun main() {
    val gameScope = CoroutineScope(Dispatchers.Default)
    val viewModelScope = CoroutineScope(Dispatchers.Default)
    val randomStateGenerator = RandomNumberGeneratorImpl()
    val gridFactory = GridFactoryImpl(randomStateGenerator)
    val gameStateFactory = GameStateFactoryImpl(gridFactory)
    val gameFactory = GameOfLifeFactory(gameStateFactory, gridFactory, gameScope)
    val gameUiStateFactory = GameUiStateFactory(gameStateFactory)
    val viewModel = GameOfLifeViewModel(gameFactory, viewModelScope, gameUiStateFactory)

    application {
        Application(viewModel, onClosed = ::exitApplication)
    }
}
