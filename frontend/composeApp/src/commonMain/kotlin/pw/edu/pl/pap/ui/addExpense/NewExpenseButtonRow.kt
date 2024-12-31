package pw.edu.pl.pap.ui.addExpense

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pw.edu.pl.pap.ui.common.TextButton
import pw.edu.pl.pap.util.constants.padding


@Composable
fun NewExpenseButtonRow(onBack: () -> Unit, onConfirm: () -> Unit, isConfirmEnabled: Boolean) {
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
            text = "ADD",
            isEnabled = isConfirmEnabled,
            modifier = Modifier.align(Alignment.BottomEnd),
            onUpdate = onConfirm
            )
    }
}