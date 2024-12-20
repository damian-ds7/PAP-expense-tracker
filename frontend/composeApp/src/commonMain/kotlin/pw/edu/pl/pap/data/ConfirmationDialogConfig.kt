package pw.edu.pl.pap.data

data class ConfirmationDialogConfig(
    val mainText: String,
    val subText: String,
    val onNo: () -> Unit,
    val onYes: () -> Unit
)
