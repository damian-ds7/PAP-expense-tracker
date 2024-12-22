package pw.edu.pl.pap.ui.home.groupingSystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import pw.edu.pl.pap.data.GroupKey

@Composable
fun GroupButton(currentGrouping: GroupKey, onClick: () -> Unit) {
    TextButton(
        onClick,
        contentPadding = ButtonDefaults.TextButtonContentPadding,
        colors = ButtonDefaults.textButtonColors(contentColor = LocalContentColor.current),
    ) {
        Text(text = currentGrouping.displayName)
        Icon(Icons.Default.Menu, contentDescription = "Group by")
    }
}