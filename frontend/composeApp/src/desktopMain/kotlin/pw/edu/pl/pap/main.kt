package pw.edu.pl.pap

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension

fun main() = application {
    val state = rememberWindowState(
        width = 600.dp,
        height = 800.dp,
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "Expense tracker",
        state = state,
    ) {
        window.minimumSize = Dimension(500, 600)
        App("http://localhost:8080")
    }
}