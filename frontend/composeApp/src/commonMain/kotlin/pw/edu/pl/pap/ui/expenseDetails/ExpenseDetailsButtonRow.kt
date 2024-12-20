package pw.edu.pl.pap.ui.expenseDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pw.edu.pl.pap.ui.common.BackButton
import pw.edu.pl.pap.ui.common.TextButton
import pw.edu.pl.pap.ui.common.DeleteButton


@Composable
fun ExpenseDetailsButtonRow(onBack: () -> Unit, onConfirm: () -> Unit, onDelete: () -> Unit, isConfirmEnabled: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BackButton(
            modifier = Modifier.align(Alignment.BottomStart),
            onUpdate = onBack
        )

        DeleteButton(
            modifier = Modifier.align(Alignment.BottomCenter),
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