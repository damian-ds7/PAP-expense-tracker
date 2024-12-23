package pw.edu.pl.pap.ui.home.sortingSystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import pw.edu.pl.pap.util.sortingSystem.GroupKey

@Composable
fun GroupButton(currentGrouping: GroupKey, onClick: () -> Unit) {
    TextButton(
        onClick,
        contentPadding = ButtonDefaults.TextButtonContentPadding,
        colors = ButtonDefaults.textButtonColors(contentColor = LocalContentColor.current),
    ) {
        Text(text = currentGrouping.displayName)
        Icon(Icons.AutoMirrored.Default.ViewList, contentDescription = "Group by")
    }
}