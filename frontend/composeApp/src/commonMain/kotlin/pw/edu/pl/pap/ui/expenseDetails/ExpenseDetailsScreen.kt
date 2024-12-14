package pw.edu.pl.pap.ui.expenseDetails

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import pw.edu.pl.pap.navigation.ExpenseDetailsScreenComponent
import pw.edu.pl.pap.ui.common.Header
import pw.edu.pl.pap.ui.common.InputFields
import pw.edu.pl.pap.ui.common.DismissChangesPopup

@Composable
fun ExpenseDetailsScreen(
    component: ExpenseDetailsScreenComponent,
) {
    val scope = rememberCoroutineScope()
    var showConfirmDialog by remember { mutableStateOf(false) }

    Header("Expense Details")
    component.setupInputFields()
    InputFields(component.inputFieldsData)

    ExpenseDetailsButtonRow(
        onBack = {
            scope.launch {
                handleBack(component) { showConfirmDialog = it }
            }
        },
        onConfirm = { scope.launch { component.confirm() } },
        onDelete = {},
        isConfirmEnabled = component.canConfirm

    )

    if (showConfirmDialog) {
        DismissChangesPopup(
            onDismiss = {
                showConfirmDialog = false
                component.onDismiss()
            },
            onConfirm = {
                showConfirmDialog = false
            }
        )
    }
}

fun handleBack(component: ExpenseDetailsScreenComponent, showConfirmDialog: (Boolean) -> Unit) {
    if (component.noChange) {
        component.onDismiss()
    } else {
        showConfirmDialog(true)
    }
}
