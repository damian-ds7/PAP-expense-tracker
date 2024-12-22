package pw.edu.pl.pap.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun LoadingPopup(
    mainText: String = "Loading...",
    subText: String = "Loading...",
    isLoading: Boolean
) {
    if (isLoading) {
        AlertDialog(
            onDismissRequest = {}, // Prevent dismissal by user actions
            title = {
                Text(mainText)
            },
            text = {
                Text(subText)
            },
            confirmButton = {}, // No confirm button for a loading dialog
            dismissButton = {}  // No dismiss button for a loading dialog
        )
    }
}
