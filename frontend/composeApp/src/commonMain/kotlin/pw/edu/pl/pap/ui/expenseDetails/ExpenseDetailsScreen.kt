package pw.edu.pl.pap.ui.expenseDetails

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import pw.edu.pl.pap.navigation.ExpenseDetailsScreenComponent
import pw.edu.pl.pap.ui.common.BackButton
import pw.edu.pl.pap.ui.common.ConfirmButton
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

    ConfirmButton("SAVE", component.canConfirm) {
        scope.launch {
            component.confirm()
        }
    }

    BackButton {
        scope.launch {
            if (component.noChange) {
                component.onBack()
            } else {
                showConfirmDialog = true
            }
        }
    }

    if (showConfirmDialog) {
        DismissChangesPopup(
            onDismiss = {
                showConfirmDialog = false
                component.onBack()
            },
            onConfirm = {
                showConfirmDialog = false
            }
        )
    }
}