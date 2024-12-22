package pw.edu.pl.pap.ui.expenseDetails

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import pw.edu.pl.pap.navigation.singleExpense.ExpenseDetailsScreenComponent
import pw.edu.pl.pap.ui.common.ConfirmationPopup
import pw.edu.pl.pap.ui.common.Header
import pw.edu.pl.pap.ui.common.InputFields

@Composable
fun ExpenseDetailsScreen(
    component: ExpenseDetailsScreenComponent,
) {
    val scope = rememberCoroutineScope()
    var confirmDialogState by remember { mutableStateOf<ConfirmationDialogState>(ConfirmationDialogState.None) }
    val dialogFactory = remember { ConfirmationDialogFactory(component) }

    Header("Expense Details")
    component.setupInputFields()
    InputFields(component.inputFieldsData)

    ExpenseDetailsButtonRow(
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

fun handleBack(component: ExpenseDetailsScreenComponent, showConfirmDialog: (ConfirmationDialogState) -> Unit) {
    if (component.noChange) {
        component.onDismiss()
    } else {
        showConfirmDialog(ConfirmationDialogState.GoBack)
    }
}
