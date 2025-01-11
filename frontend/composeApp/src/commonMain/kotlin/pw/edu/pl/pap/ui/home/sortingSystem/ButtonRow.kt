package pw.edu.pl.pap.ui.home.sortingSystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import pw.edu.pl.pap.screenComponents.mainScreens.HomeScreenComponent
import pw.edu.pl.pap.ui.common.RefreshButton
import pw.edu.pl.pap.ui.common.UserGroupButton
import pw.edu.pl.pap.util.constants.horizontalPadding

@Composable
fun ButtonRow(
    component: HomeScreenComponent,
    onGroupKeyClick: () -> Unit,
    onUserGroupClick: () -> Unit
) {
    val currentGroupKey by component.currentGroupingKey.collectAsState()
    val currentUserGroup by component.currentUserGroup.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = horizontalPadding),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            UserGroupButton(currentUserGroup, onUserGroupClick)
        }

        Row {
            GroupButton(currentGroupKey, onGroupKeyClick)

            RefreshButton {
                component.updateNavigationState(HomeScreenComponent.NavigationState.InitialLoad)
            }
        }
    }
}