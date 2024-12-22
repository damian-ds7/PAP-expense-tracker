package pw.edu.pl.pap.data.uiSetup

data class ConfirmationDialogConfig(
    val mainText: String,
    val subText: String,
    val onNo: () -> Unit,
    val onYes: () -> Unit
)
