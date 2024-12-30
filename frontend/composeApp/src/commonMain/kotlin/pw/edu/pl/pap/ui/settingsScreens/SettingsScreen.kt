package pw.edu.pl.pap.ui.settingsScreens

import androidx.compose.runtime.*
import pw.edu.pl.pap.screenComponents.mainScreens.SettingsScreenComponent
import pw.edu.pl.pap.ui.common.Header
import pw.edu.pl.pap.ui.common.InputFields
import pw.edu.pl.pap.ui.common.ConfirmationPopup
import pw.edu.pl.pap.ui.common.showConfirmationPopup

@Composable
fun SettingsScreen(component: SettingsScreenComponent) {

    Header("Settings")

    component.setupInputFields()
    InputFields(component.inputFieldsData)

    if (component.showLogOutDialog.value) {
        showConfirmationPopup(component.logOutConfirmationData)
    }
}


