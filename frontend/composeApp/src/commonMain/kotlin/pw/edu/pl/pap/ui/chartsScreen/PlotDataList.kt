package pw.edu.pl.pap.ui.chartsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pw.edu.pl.pap.util.charts.ChartData
import pw.edu.pl.pap.util.constants.horizontalPadding
import pw.edu.pl.pap.util.constants.verticalPadding
import pw.edu.pl.pap.util.formatForDisplay


@Composable
fun PlotDataList(colors: List<Color>, plotData: ChartData, currencySymbol: String) {
    val entryStyle = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Medium
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(plotData.entries.toList()) { index, entry ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = verticalPadding, horizontal = horizontalPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ColoredDot(color = colors[index])
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = entry.key,
                        style = entryStyle
                    )
                }
                Text(
                    text = "${formatForDisplay(entry.value)} $currencySymbol",
                    style = entryStyle
                )
            }
        }
    }
}

@Composable
private fun ColoredDot(color: Color) {
    Box(
        modifier = Modifier
            .size(8.dp)
            .background(color = color, shape = MaterialTheme.shapes.small)
    )
}
