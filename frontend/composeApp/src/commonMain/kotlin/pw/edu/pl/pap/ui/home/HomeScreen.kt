package pw.edu.pl.pap.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import pw.edu.pl.pap.screenComponents.mainScreens.HomeScreenComponent
import pw.edu.pl.pap.ui.common.PaginatedLazyColumn
import pw.edu.pl.pap.ui.common.UserGroupPopup
import pw.edu.pl.pap.ui.home.sortingSystem.GroupKeyPopup

private const val buffer = 5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(component: HomeScreenComponent) {
    var showGroupingKeyMenu by remember { mutableStateOf(false) }
    var showUserGroupMenu by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(component.navigationState.collectAsState().value) {
        listState.animateScrollToItem(0)
        component.getDataBasedOnState()
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    TopSection(
                        component,
                        onGroupKeyClick = { showGroupingKeyMenu = true },
                        onUserGroupClick = { showUserGroupMenu = true }
                    )

                },
                scrollBehavior = scrollBehavior,
                expandedHeight = 100.dp
            )
        }, floatingActionButton = {
            PlusButton(onUpdate = {
                component.currentUserGroup.value?.let { userGroup ->
                    component.onAddExpenseButtonClicked(userGroup)
                } ?: run {
                    println("No UserGroup is set!")
                }
            })
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            PaginatedLazyColumn(
                component = component,
                listState = listState,
                buffer = buffer,
            )
        }
    }

    AnimatedVisibility(
        visible = showGroupingKeyMenu, enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight }), exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight })
    ) {
        GroupKeyPopup(
            component = component,
            onDismiss = { showGroupingKeyMenu = false },
        )
    }


    AnimatedVisibility(
        visible = showUserGroupMenu, enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight }), exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight })
    ) {
        UserGroupPopup(
            component = component, onDismiss = { showUserGroupMenu = false })
    }
}