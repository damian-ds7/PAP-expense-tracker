package pw.edu.pl.pap.ui.chartsScreen.filterScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pw.edu.pl.pap.ui.common.TextButton
import pw.edu.pl.pap.util.constants.padding

@Composable
fun ChartsFilterScreenButtonRow(
    onBack: () -> Unit,
//    onReset: () -> Unit,
    onConfirm: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(
            text = "BACK",
            onUpdate = onBack
        )

//        TextButton(
//            text = "RESET",
//            onUpdate = onReset
//        )

        TextButton(
            text = "SAVE",
            onUpdate = onConfirm
        )
    }
}