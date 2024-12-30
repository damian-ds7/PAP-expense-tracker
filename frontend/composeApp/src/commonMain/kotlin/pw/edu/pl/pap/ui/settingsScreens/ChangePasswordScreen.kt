package pw.edu.pl.pap.ui.settingsScreens

import androidx.compose.runtime.Composable
import kotlinx.coroutines.launch
import pw.edu.pl.pap.screenComponents.settingsScreens.ChangePasswordScreenComponent
import pw.edu.pl.pap.ui.common.*

@Composable
fun ChangePasswordScreen(component: ChangePasswordScreenComponent) {
    Header("Change password")

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

    WarningPopup(
        subText = "Incorrect passwords.",
        showWarning = component.showPasswordsWarning.value,
        onDismiss = {
            component.showPasswordsWarning.value = (!component.showPasswordsWarning.value)
        }
    )


}