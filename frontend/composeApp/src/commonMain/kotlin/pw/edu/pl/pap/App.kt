package pw.edu.pl.pap

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import pw.edu.pl.pap.screenComponents.RootComponent
import pw.edu.pl.pap.ui.settingsScreens.SettingsScreen
import pw.edu.pl.pap.ui.addExpense.NewExpenseScreen
import pw.edu.pl.pap.ui.expenseDetails.ExpenseDetailsScreen
import pw.edu.pl.pap.ui.home.HomeScreen
import pw.edu.pl.pap.ui.loginSystem.LogInScreen
import pw.edu.pl.pap.ui.loginSystem.LogInSignUpSelectionScreen
import pw.edu.pl.pap.ui.loginSystem.SignUpScreen
import pw.edu.pl.pap.ui.navBar.BottomNavBar
import pw.edu.pl.pap.ui.navBar.NavBarItem
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import pw.edu.pl.pap.di.authModule
import pw.edu.pl.pap.ui.chartsScreen.ChartsScreen
import pw.edu.pl.pap.ui.chartsScreen.filterScreen.ChartsFilterScreen
import pw.edu.pl.pap.ui.groupScreens.*
import pw.edu.pl.pap.ui.settingsScreens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(rootComponent: RootComponent, baseUrl: String) {

//    val newUrl = "http://192.168.0.102:8080/"

    remember {
        startKoin {
//            properties(mapOf("baseUrl" to newUrl))
            properties(mapOf("baseUrl" to baseUrl))
            modules(authModule)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            stopKoin()
        }
    }

    val childStack = rootComponent.childStack.subscribeAsState()
    val activeNavBarItem by rootComponent.activeNavBarItem.collectAsState()
    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()


    MaterialTheme(colorScheme = darkColorScheme()) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection).systemBarsPadding(),
            bottomBar = {
                AnimatedVisibility(
                    visible = showBottomBar(childStack.value.active.instance), enter = fadeIn(), exit = fadeOut()
                ) {
                    BottomNavBar(
                        items = listOf(
                            NavBarItem.Home, NavBarItem.Charts, NavBarItem.Groups, NavBarItem.Settings
                        ),
                        selectedItem = activeNavBarItem,
                        onSelect = { rootComponent.navBarItemClicked(it) },
                        scrollBehavior = scrollBehavior
                    )
                }
            },
        ) { innerPadding ->
            SystemBarsColors(MaterialTheme.colorScheme.surfaceContainer, MaterialTheme.colorScheme.surfaceContainer)
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

                            is RootComponent.Child.ChartsScreen -> ChartsScreen(instance.component)

                            is RootComponent.Child.ChartsFilterScreen -> ChartsFilterScreen(instance.component)

                            is RootComponent.Child.GroupScreen -> GroupScreen(instance.component)

                            is RootComponent.Child.SettingsScreen -> SettingsScreen(instance.component)

                            is RootComponent.Child.MemberScreen -> MemberScreen(instance.component)
                            is RootComponent.Child.NewGroupScreen -> NewGroupScreen(instance.component)
                            is RootComponent.Child.EditGroupScreen -> EditGroupScreen(instance.component)
                            is RootComponent.Child.InvitationsScreen -> InvitationsScreen(instance.component)

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
        is RootComponent.Child.ChartsScreen,
        is RootComponent.Child.GroupScreen,
        is RootComponent.Child.SettingsScreen -> true
        else -> false
    }
}


@Composable
expect fun SystemBarsColors(statusBarColor: Color, navBarColor: Color)