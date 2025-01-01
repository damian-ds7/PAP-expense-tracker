package pw.edu.pl.pap.ui.chartsScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.koalaplot.core.pie.DefaultSlice
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.generateHueColorPalette
import pw.edu.pl.pap.screenComponents.mainScreens.ChartsScreenComponent
import pw.edu.pl.pap.ui.chartsScreen.buttonRow.ButtonRow
import pw.edu.pl.pap.ui.common.Header
import pw.edu.pl.pap.util.constants.padding

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun ChartsScreen(component: ChartsScreenComponent) {
    val plotData by component.plotData.collectAsState()

    LaunchedEffect(component.navigationState.collectAsState()) {
        component.getDataBasedOnState()
    }

    Column {
        Header("Expense data")
        ButtonRow(component)

        val data = plotData.values.toList()
        val keys = plotData.keys.toList()
        //TODO make onClick act like hover
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            PieChart(
                data,
                minPieDiameter = 200.dp,
                slice = { i: Int ->
                    val colors = remember(data.size) { generateHueColorPalette(data.size) }
                    DefaultSlice(
                        colors[i],
                        hoverExpandFactor = 1.05f,
                        hoverElement = {
                            HoverSurface {
                                Text(
                                    keys[i],
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        },
                        clickable = true,
                        onClick = {}
                    )
                },
                labelConnector = {}
            )
        }
    }
}

@Composable
fun HoverSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        shadowElevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.padding(padding)
    ) {
        Box(modifier = Modifier.padding(padding)) {
            content()
        }
    }
}