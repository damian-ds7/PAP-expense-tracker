package pw.edu.pl.pap.ui.groupScreens

import androidx.compose.runtime.Composable
import kotlinx.coroutines.launch
import pw.edu.pl.pap.screenComponents.groupScreens.InvitationsScreenComponent
import pw.edu.pl.pap.ui.common.ConfirmOrBackButtonRow
import pw.edu.pl.pap.ui.common.InputFields
import pw.edu.pl.pap.ui.common.TwoChoiceClickableHeader


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
        //TODO
    } else {

    }

}