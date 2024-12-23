package pw.edu.pl.pap.ui.home.sortingSystem

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pw.edu.pl.pap.util.sortingSystem.GroupKey
import pw.edu.pl.pap.util.sortingSystem.Order
import pw.edu.pl.pap.screenComponents.HomeScreenComponent
import pw.edu.pl.pap.ui.common.LoadingPopup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupPopup(
    component: HomeScreenComponent,
    onDismiss: () -> Unit,
) {
    val currentOrder by component.currentGroupingOrder.collectAsState()
    val selectedOption by component.currentGroupingKey.collectAsState()
    var isLoading by remember {mutableStateOf(false)}
    var pendingAction: (() -> Unit)? by remember { mutableStateOf(null) }
    println("$selectedOption - $currentOrder")

//    println("$selectedOption - $currentOrder")
    LoadingPopup(
        isLoading = isLoading
    )


    LaunchedEffect(pendingAction) {
        pendingAction?.let { action ->
            isLoading = true
            kotlinx.coroutines.delay(50)

            try {
                action()
            } finally {
                isLoading = false
                pendingAction = null
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("Group by", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.padding(16.dp))

            for (groupKey in GroupKey.entries) {
                val isSelected = selectedOption == groupKey

                val onGroupClick: () -> Unit = if (!isSelected) {
                    {
                        pendingAction = {
                            clickAction(groupKey, component)
                        }
                    }
                } else {
                    onDismiss
                }

                val onOrderClick = {
                    pendingAction = {
                        component.sortGroups()
                    }
                }

                GroupAndSortButton(groupKey, isSelected, currentOrder, onGroupClick, onOrderClick)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun GroupAndSortButton(
    groupKey: GroupKey,
    isSelected: Boolean,
    currentOrder: Order,
    onGroupKeyChange: () -> Unit,
    onOrderChange: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
    ) {
        GroupButton(groupKey, isSelected, onGroupKeyChange)
        Spacer(modifier = Modifier.weight(1f))
        if (isSelected) {
            SortButton(currentOrder, onOrderChange)
        }
    }
}

@Composable
private fun GroupButton(
    groupKey: GroupKey,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val color = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current

    Button(
        onClick = onClick,
        contentPadding = ButtonDefaults.TextButtonContentPadding,
        colors = ButtonDefaults.textButtonColors(contentColor = color)
    ) {
        Text(text = groupKey.displayName, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun SortButton(currentOrder: Order, onClick: () -> Unit) {
    val icon = if (currentOrder == Order.ASCENDING) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown

    IconButton(onClick) {
        Icon(
            imageVector = icon,
            modifier = Modifier.size(100.dp),
            contentDescription = "Sort order"
        )
    }
}

private fun clickAction(key: GroupKey, component: HomeScreenComponent) {
    component.updateGroupingKey(key)
    component.updateNavigationState(HomeScreenComponent.NavigationState.InitialLoad)
    component.getDataBasedOnState()
}
