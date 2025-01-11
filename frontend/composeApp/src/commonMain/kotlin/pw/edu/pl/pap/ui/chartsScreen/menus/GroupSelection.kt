package pw.edu.pl.pap.ui.chartsScreen.menus

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import pw.edu.pl.pap.screenComponents.mainScreens.ChartsScreenComponent

@Composable
fun GroupSelection(component: ChartsScreenComponent) {
    var isGroupMenuExpanded by remember { mutableStateOf(false) }
    val selectedGroup by component.currentUserGroup.collectAsState()
    val groups by component.userGroupInfo.collectAsState()

    Box {
        TextButton(onClick = { isGroupMenuExpanded = true }) {
            Row {
                selectedGroup?.let { Text(text = it.name) }
                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Group menu dropdown")
            }
        }
        DropdownMenu(
            expanded = isGroupMenuExpanded, onDismissRequest = { isGroupMenuExpanded = false }) {
            groups.forEachIndexed { idx, group ->
                DropdownMenuItem(text = { Text(group.name) }, onClick = {
                    component.updateCurrentUserGroup(groups[idx])
                    isGroupMenuExpanded = false
                })
            }
        }
    }
}