package pw.edu.pl.pap.ui.settingsScreens

import androidx.compose.runtime.Composable
import kotlinx.coroutines.launch
import pw.edu.pl.pap.screenComponents.settingsScreens.PreferencesScreenComponent
import pw.edu.pl.pap.screenComponents.settingsScreens.ServerAdressScreenComponent
import pw.edu.pl.pap.ui.common.ConfirmOrBackButtonRow
import pw.edu.pl.pap.ui.common.Header
import pw.edu.pl.pap.ui.common.InputFields
import pw.edu.pl.pap.ui.common.showConfirmationPopup

@Composable
fun ServerAddressScreen(component: ServerAdressScreenComponent) {
    Header("Server Address")

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