package pw.edu.pl.pap.ui.groupScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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


@Composable
fun InvitationsScreen (component: InvitationsScreenComponent) {

    component.setupNewInvitationInputFields()

    TwoChoiceClickableHeader(
        text = "  NEW  ",
        onClick = { component.isNewInvitationsScreen.value = true },
        text2 = "PENDING",
        onClick2 = { component.isNewInvitationsScreen.value = false },
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
    }

}