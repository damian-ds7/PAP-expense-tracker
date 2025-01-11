package pw.edu.pl.pap.ui.chartsScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.koalaplot.core.pie.DefaultSlice
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun Chart(colors: List<Color>, values: List<Float>) {
    PieChart(
        values,
        slice = { i: Int ->
            DefaultSlice(
                colors[i],
                antiAlias = true, //remove if bad performance
                border = if (colors.size > 1) BorderStroke(2.5.dp, Color.Black) else null
            )
        },
        labelConnector = {},
        maxPieDiameter = 500.dp
    )
}