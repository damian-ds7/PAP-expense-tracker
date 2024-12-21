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
import pw.edu.pl.pap.navigation.HomeScreenComponent
import pw.edu.pl.pap.ui.common.LoadingScreen

@Composable
fun HomeScreen(component: HomeScreenComponent) {
    var isLoading by remember { mutableStateOf(true) }
    var showGroupingMenu by remember { mutableStateOf(false) }
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
                        ButtonRow(component) { showGroupingMenu = true }
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
            visible = showGroupingMenu,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight }
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight }
            )
        ) {
            GroupPopup(
                component = component,
                onDismiss = { showGroupingMenu = false },
            )
        }

    }
}
