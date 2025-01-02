package pw.edu.pl.pap.ui.groupScreens

import androidx.compose.runtime.Composable
import pw.edu.pl.pap.screenComponents.mainScreens.GroupScreenComponent
import pw.edu.pl.pap.ui.common.Header
import pw.edu.pl.pap.ui.common.InputFields

@Composable
fun GroupScreen (component: GroupScreenComponent) {

    Header(component.currentUserGroup.name)

    component.setupInputFields()
    InputFields(component.inputFieldsData)
}