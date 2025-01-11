package pw.edu.pl.pap.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.*
import pw.edu.pl.pap.util.constants.padding

@Composable
fun PlusButton(onUpdate: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(padding),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            onClick = { onUpdate() }
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Expense",
            )
        }
    }
}
