package pw.edu.pl.pap.ui.groupScreens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import kotlinx.coroutines.runBlocking
import pw.edu.pl.pap.screenComponents.mainScreens.GroupScreenComponent
import pw.edu.pl.pap.ui.common.*
import pw.edu.pl.pap.util.constants.padding

@Composable
fun GroupScreen(component: GroupScreenComponent) {
    val currentUserGroup by component.currentUserGroup.collectAsState()

    if (currentUserGroup != null) {
        ClickableHeader(currentUserGroup?.name!!) { component.onEditGroupClicked() }

        LaunchedEffect(component.currentUserGroup.value) {
            runBlocking { component.updateUsers() }
        }

        InputFields(component.inputFieldsData)
        //TODO add text if group is empty
    } else {
        Header("None")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(2*padding),
        contentAlignment = Alignment.TopEnd
    ) {
        RefreshButton {
            component.refreshGroup()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                text = "NEW GROUP",
                onUpdate = { component.onNewGroupClicked() },
                width = 0.4f
            )

            TextButton(
                text = "INVITATIONS",
                onUpdate = { component.onInvitationsClicked() },
                width = 0.4f
            )
        }
    }
}