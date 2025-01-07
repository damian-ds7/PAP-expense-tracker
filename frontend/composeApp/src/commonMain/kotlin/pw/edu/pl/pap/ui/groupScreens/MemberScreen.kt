package pw.edu.pl.pap.ui.groupScreens

import androidx.compose.runtime.Composable
import kotlinx.coroutines.launch
import pw.edu.pl.pap.screenComponents.groupScreens.MemberScreenComponent
import pw.edu.pl.pap.screenComponents.mainScreens.GroupScreenComponent
import pw.edu.pl.pap.ui.common.ConfirmOrBackButtonRow
import pw.edu.pl.pap.ui.common.Header
import pw.edu.pl.pap.ui.common.InputFields
import pw.edu.pl.pap.ui.common.showConfirmationPopup

@Composable
fun MemberScreen (component: MemberScreenComponent) {

    Header("${component.user.name} ${component.user.surname}")

    component.setupInputFields()
    InputFields(component.inputFieldsData)

    ConfirmOrBackButtonRow(
        text = "CONFIRM",
        onBack = { component.coroutineScope.launch { component.onBack() } },
        onConfirm = { component.showChangeRoleConfirmationDialog.value = true },
        isConfirmEnabled = component.canConfirm
    )

    if (component.showChangeRoleConfirmationDialog.value) {
        showConfirmationPopup(component.changeRoleConfirmationData)
    }

    if (component.showKickConfirmationDialog.value) {
        showConfirmationPopup(component.kickConfirmationData)
    }
}