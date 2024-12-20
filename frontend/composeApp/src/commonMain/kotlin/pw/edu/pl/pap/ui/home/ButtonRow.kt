package pw.edu.pl.pap.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pw.edu.pl.pap.navigation.HomeScreenComponent

@Composable
fun ButtonRow(component: HomeScreenComponent, onGroupClick: () -> Unit) {
    val currentGroupKey by component.currentGroupKey.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        GroupButton(currentGroupKey, onGroupClick)

        RefreshButton {
            component.updateNavigationState(HomeScreenComponent.NavigationState.InitialLoad)
            component.getDataBasedOnState()
        }
    }
}