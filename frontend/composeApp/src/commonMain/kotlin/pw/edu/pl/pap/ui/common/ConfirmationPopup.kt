package pw.edu.pl.pap.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import pw.edu.pl.pap.data.uiSetup.ConfirmationDialogConfig

@Composable
fun ConfirmationPopup(
    mainText: String,
    subText: String,
    onNo: () -> Unit,
    onYes: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(mainText)
        }, text = {
            Text(subText)
        }, confirmButton = {
            TextButton(
                onClick = {
                    onNo()
                }) {
                Text("No")
            }
        }, dismissButton = {
            TextButton(
                onClick = { onYes() },
            ) {
                Text("Yes")
            }
        })
}

@Composable
fun showConfirmationPopup(data: ConfirmationDialogConfig) {
    ConfirmationPopup(
        mainText = data.mainText,
        subText = data.subText,
        onNo = {
            data.onNo()
        },
        onYes = {
            data.onYes()
        }
    )
}
