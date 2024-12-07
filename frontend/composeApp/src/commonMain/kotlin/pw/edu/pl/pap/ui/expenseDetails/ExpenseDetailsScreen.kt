package pw.edu.pl.pap.ui.expenseDetails

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import pw.edu.pl.pap.data.Expense
import pw.edu.pl.pap.navigation.ExpenseDetailsScreenComponent
import pw.edu.pl.pap.ui.common.BackButton
import pw.edu.pl.pap.ui.common.ConfirmButton
import pw.edu.pl.pap.ui.common.Header
import pw.edu.pl.pap.ui.common.InputFields
import pw.edu.pl.pap.util.ConfirmChangesPopup

@Composable
fun ExpenseDetailsScreen(
    component: ExpenseDetailsScreenComponent,
) {
    val scope = rememberCoroutineScope()
    var showConfirmDialog by remember { mutableStateOf(false) }

    Header("Expense Details")
    component.setupInputFields()
    InputFields(component.inputFieldsData)

    ConfirmButton("SAVE") {
        scope.launch {
            component.confirmChanges()
        }
    }

    BackButton {
        scope.launch {
//            component.onBack()
            showConfirmDialog = true
        }
    }

    if (showConfirmDialog) {
        ConfirmChangesPopup(
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