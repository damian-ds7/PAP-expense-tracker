package pw.edu.pl.pap.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pw.edu.pl.pap.util.constants.padding

@Composable
fun ConfirmOrBackButtonRow(
    text: String,
    onBack: () -> Unit,
    onConfirm: () -> Unit
) {

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
            text = text,
            modifier = Modifier.align(Alignment.BottomEnd),
            onUpdate = onConfirm
        )
    }
}