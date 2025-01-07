package pw.edu.pl.pap.ui.groupScreens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import pw.edu.pl.pap.screenComponents.groupScreens.groupEdit.NewGroupScreenComponent
import pw.edu.pl.pap.ui.common.ConfirmOrBackButtonRow
import pw.edu.pl.pap.ui.common.Header
import pw.edu.pl.pap.ui.common.InputFields

@Composable
fun NewGroupScreen(
    component: NewGroupScreenComponent
){
    val scope = rememberCoroutineScope()

    Header("New group")
    component.setupInputFields()
    InputFields(component.inputFieldsData)

    ConfirmOrBackButtonRow(
        text = "ADD",
        onBack = { scope.launch { component.onDismiss() } },
        onConfirm = { scope.launch { component.confirm() } },
        isConfirmEnabled = component.canConfirm
    )
}