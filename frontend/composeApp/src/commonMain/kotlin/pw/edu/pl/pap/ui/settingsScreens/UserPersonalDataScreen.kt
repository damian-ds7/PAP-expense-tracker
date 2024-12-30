package pw.edu.pl.pap.ui.settingsScreens

import androidx.compose.runtime.Composable
import kotlinx.coroutines.launch
import pw.edu.pl.pap.screenComponents.settingsScreens.UserPersonalDataScreenComponent
import pw.edu.pl.pap.ui.common.ConfirmOrBackButtonRow
import pw.edu.pl.pap.ui.common.Header
import pw.edu.pl.pap.ui.common.InputFields
import pw.edu.pl.pap.ui.common.showConfirmationPopup

@Composable
fun UserPersonalDataScreen(component: UserPersonalDataScreenComponent) {

    Header("Personal data")

    component.setupInputFields()
    InputFields(component.inputFieldsData)

    ConfirmOrBackButtonRow(
        text = "CONFIRM",
        onBack = { component.coroutineScope.launch { component.onBack() } },
        onConfirm = { component.onConfirmClicked() }
    )

    if (component.showConfirmationDialog.value) {
        showConfirmationPopup(component.confirmationData)
    }
}