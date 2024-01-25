# About

Yet Another Implementation of Game of Life (YAIoGoL) is a simple implementation of Game of Life in Compose Multiplatform.

https://github.com/welniak/YAIoGoL/assets/13221950/bdba1ec5-309c-4034-be09-4af459521e1d

The game follows the classic rules.

Read more about the Game of Life: https://en.wikipedia.org/wiki/Conway's_Game_of_Life

# How to run it

Clone the code and run the `main` function in `src/jvmMain/kotlin/Main.kt` file.

# User manual

1. Settings
   - **Generation duration:** pick how long each duratrion should last, can be adjusted while the game is running.
   - **Grid size:** pick the size of the grid; note that the grid is not wrapping around, the cells can't interact over the edges of the grid.
   - **Randomize:** pick a random initial state of the grid.
   - **Clear grid:** make all cells in the grid dead.
   - **Reset view:** reset the zoom and pan of the grid to the initial position.
   - **Start:** start the game from the current state of the grid.
   - **Stop:** stop the current game.
   - **Pause:** pause the current game.
  
2. Controls
   - Left click on the cell to switch between live and dead states.
   - Hold the right mouse button and move the coursor to pan around the grid.
   - Use the scroll to zoom in and out the grid.
