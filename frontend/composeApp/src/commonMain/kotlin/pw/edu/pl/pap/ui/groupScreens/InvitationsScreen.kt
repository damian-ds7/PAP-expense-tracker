package pw.edu.pl.pap.ui.groupScreens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pw.edu.pl.pap.screenComponents.groupScreens.InvitationsScreenComponent
import pw.edu.pl.pap.ui.common.ConfirmOrBackButtonRow
import pw.edu.pl.pap.ui.common.InputFields
import pw.edu.pl.pap.ui.common.TextButton
import pw.edu.pl.pap.ui.common.TwoChoiceClickableHeader
import pw.edu.pl.pap.util.constants.padding
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@Composable
fun InvitationsScreen (component: InvitationsScreenComponent) {

    component.setupNewInvitationInputFields()

    TwoChoiceClickableHeader(
        text = "  NEW  ",
        onClick = { component.isNewInvitationsScreen.value = true },
        text2 = "PENDING",
        onClick2 = {
            component.isNewInvitationsScreen.value = false
            component.fetchCurrentInvites()
       },
        isHighlighted = component.isNewInvitationsScreen.value
    )

    if (component.isNewInvitationsScreen.value && !component.isPostSearchClicked.value){
        InputFields(component.newInvitationInputFieldsData)

        ConfirmOrBackButtonRow(
            text = "SEARCH",
            onBack = { component.coroutineScope.launch { component.onDismiss() } },
            onConfirm = { component.coroutineScope.launch { component.search() } }
        )
    } else if (component.isNewInvitationsScreen.value && component.isPostSearchClicked.value) {
        InvitationFields(
            component.availableNewInvitationsData,
            Modifier.offset(x = 0.dp, y = 100.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TextButton(
                text = "BACK",
                modifier = Modifier.align(Alignment.BottomStart),
                onUpdate = { component.coroutineScope.launch { component.isPostSearchClicked.value = false } },
            )
        }
    } else {
        component.isPostSearchClicked.value = false

        var offset: Int = 100

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "RECEIVED",
                fontSize = 22.sp,
                fontWeight = FontWeight.Thin,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = 0.dp, y = offset.dp)
            )
        }

        offset += 40

        if (component.receivedInvitationData.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "You have 0 received invitations currently ",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(x = 0.dp, y = offset.dp)
                )
            }
            offset += 40
        } else {
            InvitationFields(
                component.receivedInvitationData,
                Modifier.offset(x = 0.dp, y = 140.dp)
            )
            offset += (component.receivedInvitationData.size * 58)
        }

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "SENT",
                fontSize = 22.sp,
                fontWeight = FontWeight.Thin,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = 0.dp, y = offset.dp)
            )
        }

        offset += 40

        if (component.sentInvitationData.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "You have 0 sent invitations currently ",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(x = 0.dp, y = offset.dp)
                )
            }
            offset += 40
        } else {
            InvitationFields(
                component.sentInvitationData,
                Modifier.offset(x = 0.dp, y = offset.dp)
            )
        }
    }
}