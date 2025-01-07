package pw.edu.pl.pap.ui.common.DialogFactory

sealed class ConfirmationDialogState {
    data object None: ConfirmationDialogState()
    data object Delete: ConfirmationDialogState()
    data object GoBack: ConfirmationDialogState()
}