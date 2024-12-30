package pw.edu.pl.pap.ui.home.sortingSystem

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.util.sortingSystem.GroupKey
import pw.edu.pl.pap.util.sortingSystem.Order
import pw.edu.pl.pap.screenComponents.mainScreens.HomeScreenComponent
import pw.edu.pl.pap.ui.common.LoadingPopup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserGroupPopup(
    component: HomeScreenComponent,
    onDismiss: () -> Unit,
) {
    val selectedOption by component.currentUserGroup.collectAsState()
    val userGroupsInfo by component.userGroupInfo.collectAsState()
    var isLoading by remember {mutableStateOf(false)}
    var pendingAction: (() -> Unit)? by remember { mutableStateOf(null) }
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
            Text("User group", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.padding(16.dp))

            userGroupsInfo?.forEach { userGroup ->
                val isSelected = selectedOption?.name == userGroup.name

                val onGroupClick: () -> Unit = if (!isSelected) {
                    {
                        pendingAction = {
                            clickAction(userGroup, component)
                        }
                    }
                } else {
                    onDismiss
                }

                GroupButton(userGroup, isSelected, onGroupClick)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun GroupButton(
    userGroup: UserGroup,
    isSelected: Boolean,
    onGroupChange: () -> Unit,
){
    val color = if (isSelected) MaterialTheme.colorScheme.primary else LocalContentColor.current

    Button(
        onClick = onGroupChange,
        contentPadding = ButtonDefaults.TextButtonContentPadding,
        colors = ButtonDefaults.textButtonColors(contentColor = color)
    ) {
        userGroup.name?.let { Text(text = it, style = MaterialTheme.typography.bodyLarge) }
    }
}

private fun clickAction(userGroup: UserGroup, component: HomeScreenComponent) {
    component.updateUserGroup(userGroup)
    component.updateNavigationState(HomeScreenComponent.NavigationState.InitialLoad)
    component.getDataBasedOnState()
}

