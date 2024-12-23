package pw.edu.pl.pap.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pw.edu.pl.pap.screenComponents.HomeScreenComponent
import pw.edu.pl.pap.ui.common.LoadingScreen
import pw.edu.pl.pap.ui.home.sortingSystem.ButtonRow
import pw.edu.pl.pap.ui.home.sortingSystem.GroupKeyPopup
import pw.edu.pl.pap.ui.home.sortingSystem.UserGroupPopup

@Composable
fun HomeScreen(component: HomeScreenComponent) {
    var isLoading by remember { mutableStateOf(true) }
    var showGroupingKeyMenu by remember { mutableStateOf(false) }
    var showUserGroupMenu by remember { mutableStateOf(false) }
//    val homeInfo by component.homeInfo.collectAsState()
//    val groupedExpenses by component.groupedExpenses.collectAsState()

    LaunchedEffect(component.navigationState.collectAsState()) {
        component.getDataBasedOnState()
        isLoading = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> LoadingScreen()

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    item {
                        TopSection(component)
                    }

                    item {
                        ButtonRow(
                            component = component,
                            onGroupKeyClick = { showGroupingKeyMenu = true },
                            onUserGroupClick = { showUserGroupMenu = true }
                        )
                    }

                    item {
                        GroupedExpensesList(
                            component,
                            onExpenseClick = { expense ->
                                component.onExpenseClick(expense)
                            })
                    }
                }
                PlusButton(onUpdate = { component.onAddExpenseButtonClicked() })
            }

//            else -> Text(
//                text = "No data available", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center
//            )
        }


        AnimatedVisibility(
            visible = showGroupingKeyMenu,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight }
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight }
            )
        ) {
            GroupKeyPopup(
                component = component,
                onDismiss = { showGroupingKeyMenu = false },
            )
        }


        AnimatedVisibility(
            visible = showUserGroupMenu,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight }
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight }
            )
        ) {
            UserGroupPopup(
                component = component,
                onDismiss = { showUserGroupMenu = false }
            )
        }
    }
}
