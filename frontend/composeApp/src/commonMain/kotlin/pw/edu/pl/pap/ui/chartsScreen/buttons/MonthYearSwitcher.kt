package pw.edu.pl.pap.ui.chartsScreen.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MonthYearSwitcher(
    text: @Composable () -> Unit,
    onLeftArrowClick: () -> Unit,
    onRightArrowClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = onLeftArrowClick) {
            Icon(Icons.Filled.ChevronLeft, contentDescription = "Previous time frame")
        }
        Box(
            modifier = Modifier.width(130.dp),
            contentAlignment = Alignment.Center
        ) {
            text()
        }
        IconButton(onClick = onRightArrowClick) {
            Icon(Icons.Filled.ChevronRight, contentDescription = "Next time frame")
        }
    }
}