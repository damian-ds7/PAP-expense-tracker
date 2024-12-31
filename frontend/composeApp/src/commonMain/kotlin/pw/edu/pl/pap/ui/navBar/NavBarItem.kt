package pw.edu.pl.pap.ui.navBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavBarItem(
    val unselectedImage: ImageVector,
    val selectedImage: ImageVector,
    val title: String
) {
    data object Home : NavBarItem(
        unselectedImage = Icons.Outlined.Home,
        selectedImage = Icons.Filled.Home,
        title = "Home"
    )

    data object Groups : NavBarItem(
        unselectedImage = Icons.Outlined.Groups,
        selectedImage = Icons.Filled.Groups,
        title = "Groups"
    )

    data object Charts : NavBarItem(
        unselectedImage = Icons.Outlined.Leaderboard,
        selectedImage = Icons.Filled.Leaderboard,
        title = "Data"
    )

    data object Settings : NavBarItem(
        unselectedImage = Icons.Outlined.Settings,
        selectedImage = Icons.Filled.Settings,
        title = "Settings"
    )
}