package ui.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import game.model.*
import kotlin.time.Duration
import kotlin.time.DurationUnit

@Composable
fun PropertySlider(
    initialValue: Float,
    minValue: Float,
    maxValue: Float,
    label: String,
    enabled: Boolean = true,
    selectedValueLabelFormatter: (Float) -> String,
    onChanged: (Float) -> Unit,
) {
    var value by remember { mutableStateOf(initialValue) }

    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Row {
            Text(text = "$label: ", fontSize = 14.sp)
            Text(text = selectedValueLabelFormatter(value), fontSize = 14.sp)
        }
        Slider(
            value = value,
            valueRange = minValue..maxValue,
            onValueChange = {
                value = it
                onChanged(value)
            },
            enabled = enabled,
            onValueChangeFinished = { onChanged(value) }
        )
    }
}

@Composable
fun SettingsPanel(
    generationDuration: Duration,
    gridSize: Int,
    gameStatus: GameStatus,
    onGenerationDurationChanged: (Long) -> Unit,
    onStartPauseButtonClicked: () -> Unit,
    onStopButtonClicked: () -> Unit,
    onRandomizeButtonClicked: () -> Unit,
    onClearGridButtonClicked: () -> Unit,
    onResetViewButtonClicked: () -> Unit,
    onGridSizeChanged: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.width(400.dp).padding(start = 8.dp)
    ) {
        item {
            Text(
                "Settings",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp)
            )
        }
        item {
            PropertySlider(
                initialValue = generationDuration.toDouble(DurationUnit.SECONDS).toFloat(),
                minValue = MinGenerationDuration.toDouble(DurationUnit.SECONDS).toFloat(),
                maxValue = MaxGenerationDuration.toDouble(DurationUnit.SECONDS).toFloat(),
                "Generation duration in s",
                selectedValueLabelFormatter = { it.toString() }
            ) { newValue -> onGenerationDurationChanged((newValue * 1000L).toLong()) }
        }
        item {
            PropertySlider(
                initialValue = gridSize.toFloat(),
                minValue = MinGridSize.toFloat(),
                maxValue = MaxGridSize.toFloat(),
                "Grid size",
                enabled = gameStatus == GameStatus.Initial,
                selectedValueLabelFormatter = { "${it.toInt()}x${it.toInt()} "}
            ) { newValue -> onGridSizeChanged(newValue.toInt()) }
        }
        item {
            TextButton(
                onClick = onRandomizeButtonClicked,
                enabled = gameStatus == GameStatus.Initial
            ) {
                Text("Randomize")
            }
        }
        item {
            TextButton(
                onClick = onClearGridButtonClicked,
                enabled = gameStatus == GameStatus.Initial
            ) {
                Text("Clear grid")
            }
        }
        item {
            TextButton(
                onClick = onResetViewButtonClicked
            ) {
                Text("Reset view")
            }
        }
        item {
            Row {
                TextButton(
                    onClick = onStartPauseButtonClicked,
                ) {
                    Text(
                        if (gameStatus == GameStatus.Started) "Pause" else "Start"
                    )
                }
                TextButton(
                    onClick = onStopButtonClicked,
                    enabled = gameStatus == GameStatus.Started || gameStatus == GameStatus.Paused
                ) {
                    Text("Stop")
                }
            }

        }
    }
}
