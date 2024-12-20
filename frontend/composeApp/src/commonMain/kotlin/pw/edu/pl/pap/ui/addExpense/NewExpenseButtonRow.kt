package pw.edu.pl.pap.ui.addExpense

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pw.edu.pl.pap.ui.common.BackButton
import pw.edu.pl.pap.ui.common.TextButton


@Composable
fun NewExpenseButtonRow(onBack: () -> Unit, onConfirm: () -> Unit, isConfirmEnabled: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextButton(
            text = "BACK",
            modifier = Modifier.align(Alignment.BottomStart),
            onUpdate = onBack
        )

        TextButton(
            text = "ADD",
            isEnabled = isConfirmEnabled,
            modifier = Modifier.align(Alignment.BottomEnd),
            onUpdate = onConfirm
            )
    }
}