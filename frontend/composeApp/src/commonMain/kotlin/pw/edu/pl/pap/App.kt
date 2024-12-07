package pw.edu.pl.pap

import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import pw.edu.pl.pap.apiclient.ApiClient
import pw.edu.pl.pap.navigation.RootComponent
import pw.edu.pl.pap.ui.home.HomeScreen
import pw.edu.pl.pap.ui.addExpense.NewExpenseScreen
import pw.edu.pl.pap.viewmodel.HomeViewModel

@Composable
fun App(rootComponent: RootComponent) {




    MaterialTheme(colorScheme = darkColorScheme()) {
        val childStack = rootComponent.childStack.subscribeAsState()
        Children(
            stack = childStack.value,
            animation = stackAnimation(slide()),
        ) { child ->
            when (val instance = child.instance) {
                is RootComponent.Child.HomeScreen ->
                    HomeScreen(instance.component)

                is RootComponent.Child.NewExpenseScreen ->
                    NewExpenseScreen(instance.component)
            }
        }
    }
}
