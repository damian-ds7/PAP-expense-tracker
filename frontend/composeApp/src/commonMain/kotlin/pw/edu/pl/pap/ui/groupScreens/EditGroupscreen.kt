package pw.edu.pl.pap.ui.groupScreens

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import pw.edu.pl.pap.screenComponents.groupScreens.EditGroupScreenComponent
import pw.edu.pl.pap.ui.common.BackDeleteAddButtonRow
import pw.edu.pl.pap.ui.common.ConfirmationPopup
import pw.edu.pl.pap.ui.common.DialogFactory.ConfirmationDialogFactory
import pw.edu.pl.pap.ui.common.Header
import pw.edu.pl.pap.ui.common.InputFields
import pw.edu.pl.pap.ui.common.DialogFactory.ConfirmationDialogState

@Composable
fun EditGroupScreen(
    component: EditGroupScreenComponent,
) {
    val scope = rememberCoroutineScope()
    var confirmDialogState by remember { mutableStateOf<ConfirmationDialogState>(ConfirmationDialogState.None) }
    val dialogFactory = remember { ConfirmationDialogFactory(
        onDismiss = component.onDismiss,
        onDelete = { component.deleteGroup() }
    ) }

    Header("Edit Group")
    component.setupInputFields()
    InputFields(component.inputFieldsData)

    BackDeleteAddButtonRow(
        onBack = {
            scope.launch {
                handleBack(component) { confirmDialogState = ConfirmationDialogState.GoBack }
            }
        },
        onConfirm = {
            scope.launch {
                component.confirm()
            }
        },
        onDelete = {
            confirmDialogState = ConfirmationDialogState.Delete
        },
        isConfirmEnabled = component.canConfirm
    )

    dialogFactory.create(confirmDialogState)?.let { config ->
        ConfirmationPopup(
            mainText = config.mainText,
            subText = config.subText,
            onNo = {
                confirmDialogState = ConfirmationDialogState.None
                config.onNo
            },
            onYes = {
                scope.launch {
                    confirmDialogState = ConfirmationDialogState.None
                    config.onYes()
                }
            }
        )
    }
}

fun handleBack(component: EditGroupScreenComponent, showConfirmDialog: (ConfirmationDialogState) -> Unit) {
    if (component.noChange) {
        component.onDismiss()
    } else {
        showConfirmDialog(ConfirmationDialogState.GoBack)
    }
}