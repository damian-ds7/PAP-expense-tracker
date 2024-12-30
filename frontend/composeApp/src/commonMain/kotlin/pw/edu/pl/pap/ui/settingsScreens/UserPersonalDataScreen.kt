package pw.edu.pl.pap.ui.settingsScreens

import androidx.compose.runtime.Composable
import pw.edu.pl.pap.screenComponents.settingsScreens.UserPersonalDataScreenComponent
import pw.edu.pl.pap.ui.common.Header
import pw.edu.pl.pap.ui.common.InputFields

@Composable
fun UserPersonalDataScreen(component: UserPersonalDataScreenComponent) {

    Header("Personal data")

    component.setupInputFields()
    InputFields(component.inputFieldsData)


}