package pw.edu.pl.pap.ui.settingsScreen

import androidx.compose.runtime.*
import pw.edu.pl.pap.screenComponents.mainScreens.SettingsScreenComponent
import pw.edu.pl.pap.ui.common.Header
import pw.edu.pl.pap.ui.common.InputFields
import pw.edu.pl.pap.ui.common.ConfirmationPopup

@Composable
fun SettingsScreen(component: SettingsScreenComponent) {

    Header("Settings")

    component.setupInputFields()
    InputFields(component.inputFieldsData)

    if (component.showLogOutDialog.value) {
        ConfirmationPopup(
            mainText = component.logOutConfirmationData.mainText,
            subText = component.logOutConfirmationData.subText,
            onNo = {
                component.logOutConfirmationData.onNo()
            },
            onYes = {
                component.logOutConfirmationData.onYes()
            }
        )
    }
}


