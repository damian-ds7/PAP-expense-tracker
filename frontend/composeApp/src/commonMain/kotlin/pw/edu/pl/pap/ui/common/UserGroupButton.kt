package pw.edu.pl.pap.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup

@Composable
fun UserGroupButton(currentGroup: UserGroup?, onClick: () -> Unit) {
    TextButton(
        onClick,
        contentPadding = ButtonDefaults.TextButtonContentPadding,
        colors = ButtonDefaults.textButtonColors(contentColor = LocalContentColor.current),
    ) {
        Icon(Icons.Filled.Group, contentDescription = "Group by")
        Text(text = currentGroup?.name?.ifEmpty { "None" } ?: "None")
    }
}