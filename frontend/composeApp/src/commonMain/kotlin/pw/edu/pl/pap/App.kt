package pw.edu.pl.pap

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import pw.edu.pl.pap.screenComponents.RootComponent
import pw.edu.pl.pap.ui.dataScreen.DataScreen
import pw.edu.pl.pap.ui.addExpense.NewExpenseScreen
import pw.edu.pl.pap.ui.expenseDetails.ExpenseDetailsScreen
import pw.edu.pl.pap.ui.home.HomeScreen
import pw.edu.pl.pap.ui.loginSystem.LogInScreen
import pw.edu.pl.pap.ui.loginSystem.LogInSignUpSelectionScreen
import pw.edu.pl.pap.ui.loginSystem.SignUpScreen
import pw.edu.pl.pap.ui.navBar.BottomNavBar
import pw.edu.pl.pap.ui.navBar.NavBarItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import pw.edu.pl.pap.ui.settingsScreens.*

// Todo refactor function, tweak animations
@Composable
fun App(rootComponent: RootComponent) {
    val childStack = rootComponent.childStack.subscribeAsState()
    val activeNavBarItem by rootComponent.activeNavBarItem.collectAsState()

    MaterialTheme(colorScheme = darkColorScheme()) {
        Scaffold(
            bottomBar = {
                AnimatedVisibility(
                    visible = showBottomBar(childStack.value.active.instance), enter = fadeIn(), exit = fadeOut()
                ) {
                    BottomNavBar(
                        items = listOf(
                            NavBarItem.Home, NavBarItem.Data, NavBarItem.Groups, NavBarItem.Settings
                        ),
                        selectedItem = activeNavBarItem,
                        onSelect = { rootComponent.navBarItemClicked(it) }
                    )
                }
            },
        ) { innerPadding ->
            AnimatedContent(
                targetState = childStack.value.active.instance
            ) { targetInstance ->

                val padding = if (showBottomBar(targetInstance)) innerPadding else PaddingValues(0.dp)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Children(
                        stack = childStack.value,
                        animation = stackAnimation(slide()),
                    ) { child ->
                        when (val instance = child.instance) {
                            is RootComponent.Child.LogInSignUpSelectionScreen -> LogInSignUpSelectionScreen(instance.component)
                            is RootComponent.Child.LogInScreen -> LogInScreen(instance.component)
                            is RootComponent.Child.SignUpScreen -> SignUpScreen(instance.component)

                            is RootComponent.Child.HomeScreen -> HomeScreen(instance.component)
                            is RootComponent.Child.NewExpenseScreen -> NewExpenseScreen(instance.component)
                            is RootComponent.Child.ExpenseDetailsScreen -> ExpenseDetailsScreen(instance.component)

                            is RootComponent.Child.DataScreen -> DataScreen(instance.component)
                            is RootComponent.Child.SettingsScreen -> SettingsScreen(instance.component)

                            is RootComponent.Child.ServerAddressScreen -> ServerAddressScreen(instance.component)
                            is RootComponent.Child.UserPersonalDataScreen -> UserPersonalDataScreen(instance.component)
                            is RootComponent.Child.ChangePasswordScreen -> ChangePasswordScreen(instance.component)
                            is RootComponent.Child.PreferencesScreen -> PreferencesScreen(instance.component)
                        }
                    }
                }
            }
        }
    }
}

fun showBottomBar(instance: RootComponent.Child): Boolean {
    return when (instance) {
        is RootComponent.Child.HomeScreen,
        is RootComponent.Child.DataScreen -> true
        is RootComponent.Child.SettingsScreen -> true
        else -> false
    }
}
