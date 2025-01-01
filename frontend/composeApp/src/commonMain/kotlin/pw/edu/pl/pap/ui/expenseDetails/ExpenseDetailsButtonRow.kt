package pw.edu.pl.pap.ui.expenseDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pw.edu.pl.pap.ui.common.TextButton
import pw.edu.pl.pap.util.constants.padding


@Composable
fun ExpenseDetailsButtonRow(onBack: () -> Unit, onConfirm: () -> Unit, onDelete: () -> Unit, isConfirmEnabled: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        TextButton(
            text = "BACK",
            modifier = Modifier.align(Alignment.BottomStart),
            onUpdate = onBack
        )

        TextButton(
            text = "DELETE",
            modifier = Modifier.align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error, contentColor = MaterialTheme.colorScheme.onError
            ),
            onUpdate = onDelete
        )

        TextButton(
            text = "SAVE",
            isEnabled = isConfirmEnabled,
            modifier = Modifier.align(Alignment.BottomEnd),
            onUpdate = onConfirm
        )
    }
}