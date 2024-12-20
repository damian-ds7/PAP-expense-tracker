package pw.edu.pl.pap.ui.expenseDetails

sealed class ConfirmationDialogState {
    data object None: ConfirmationDialogState()
    data object Delete: ConfirmationDialogState()
    data object GoBack: ConfirmationDialogState()
}