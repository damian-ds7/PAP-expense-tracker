package pw.edu.pl.pap.ui.home.sortingSystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.util.sortingSystem.GroupKey

@Composable
fun UserGroupButton(currentGroup: UserGroup?, onClick: () -> Unit) {
    TextButton(
        onClick,
        contentPadding = ButtonDefaults.TextButtonContentPadding,
        colors = ButtonDefaults.textButtonColors(contentColor = LocalContentColor.current),
    ) {
        Text(text = currentGroup?.name?.ifEmpty { "None" } ?: "None")
        Icon(Icons.Default.Menu, contentDescription = "Group by")
    }
}