package pw.edu.pl.pap.ui.groupScreens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pw.edu.pl.pap.screenComponents.mainScreens.GroupScreenComponent
import pw.edu.pl.pap.ui.common.ClickableHeader
import pw.edu.pl.pap.ui.common.InputFields
import pw.edu.pl.pap.ui.common.TextButton
import pw.edu.pl.pap.util.constants.padding

@Composable
fun GroupScreen(component: GroupScreenComponent) {
    val currentUserGroup by component.currentUserGroup.collectAsState()

    ClickableHeader(currentUserGroup?.name!!) { component.onEditGroupClicked() }

    InputFields(component.inputFieldsData)
    //TODO add text if group is empty

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
                changeSize = true,
                modifier = Modifier
                    .width(180.dp).height(60.dp)
            )

            TextButton(
                text = "INVITATIONS",
                onUpdate = { component.onInvitationsClicked() },
                changeSize = true,
                modifier = Modifier
                    .width(180.dp).height(60.dp)
            )
        }
    }
}