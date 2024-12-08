package pw.edu.pl.pap.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DismissChangesPopup(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = "Dismiss Changes")
        }, text = {
            Text("Are you sure you want to dismiss these changes?")
        }, confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }) {
                Text("No")
            }
        }, dismissButton = {
            TextButton(
                onClick = { onDismiss() },
            ) {
                Text("Yes")
            }
        })
}
