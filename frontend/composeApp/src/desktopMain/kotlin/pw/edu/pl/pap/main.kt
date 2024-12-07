package pw.edu.pl.pap

import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import pw.edu.pl.pap.navigation.RootComponent
import java.awt.Dimension

fun main() = application {
    val state = rememberWindowState(
        width = 600.dp,
        height = 800.dp,
    )

    val lifecycle = LifecycleRegistry()
    val rootComponent = remember {
        RootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            baseUrl = "http://localhost:8080"
        )
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Expense tracker",
        state = state,
    ) {
        window.minimumSize = Dimension(500, 600)
        App(rootComponent)
    }
}