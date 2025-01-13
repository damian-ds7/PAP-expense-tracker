package pw.edu.pl.pap.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
actual fun getWindowSize(): DpSize {

    val width = 600.dp
    val height = 800.dp

    val density = LocalDensity.current
    return with(density) {
        DpSize(width, height)
    }
}