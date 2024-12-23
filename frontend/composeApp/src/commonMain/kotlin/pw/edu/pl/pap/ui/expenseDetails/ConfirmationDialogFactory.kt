package pw.edu.pl.pap.ui.expenseDetails

import pw.edu.pl.pap.data.uiSetup.ConfirmationDialogConfig
import pw.edu.pl.pap.screenComponents.singleExpense.ExpenseDetailsScreenComponent

class ConfirmationDialogFactory(private val component: ExpenseDetailsScreenComponent) {

    fun create(state: ConfirmationDialogState): ConfirmationDialogConfig? {
        return when(state) {
            ConfirmationDialogState.GoBack -> ConfirmationDialogConfig(
                mainText = "Dismiss Changes",
                subText = "Are you sure you want to dismiss these changes?",
                onNo = {},
                onYes = {
                    component.onDismiss()
                }
            )
            ConfirmationDialogState.Delete -> ConfirmationDialogConfig(
                mainText = "Delete Expense",
                subText = "Are you sure you want to delete this expense?",
                onNo = {},
                onYes = {
                    component.deleteExpense()
                }
            )
            ConfirmationDialogState.None -> null
        }
    }
}