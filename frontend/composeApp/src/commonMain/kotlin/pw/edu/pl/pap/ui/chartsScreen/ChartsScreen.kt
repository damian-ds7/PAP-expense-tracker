package pw.edu.pl.pap.ui.chartsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.koalaplot.core.util.generateHueColorPalette
import pw.edu.pl.pap.screenComponents.mainScreens.ChartsScreenComponent
import pw.edu.pl.pap.ui.chartsScreen.buttons.ButtonRow
import pw.edu.pl.pap.ui.chartsScreen.menus.GroupSelection
import pw.edu.pl.pap.ui.chartsScreen.menus.TabBar
import pw.edu.pl.pap.util.constants.horizontalPadding
import pw.edu.pl.pap.util.formatForDisplay


@Composable
fun ChartsScreen(component: ChartsScreenComponent) {
    val plotData by component.plotData.collectAsState()
    val colors = remember(plotData.size) { generateHueColorPalette(plotData.size) }

    LaunchedEffect(component.navigationState.collectAsState().value) {
        component.getDataBasedOnState()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabBar(component)

        ButtonRow(component)

        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f),
            contentAlignment = Alignment.Center
        ) {
            if (plotData == emptyMap<String, Float>()) {
                Text(text = "No data to display")
            } else {
                Chart(
                    colors,
                    plotData.values.toList(),
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box {
                GroupSelection(component)
            }
            Text(
                text = "Total: ${formatForDisplay(component.getTotal().toFloat())} zł",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        PlotDataList(colors, plotData)
    }
}