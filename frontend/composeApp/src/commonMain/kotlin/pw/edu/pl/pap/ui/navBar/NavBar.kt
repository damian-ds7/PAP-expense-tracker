package pw.edu.pl.pap.ui.navBar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pw.edu.pl.pap.util.constants.horizontalPadding
import pw.edu.pl.pap.util.constants.verticalPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    items: List<NavBarItem>,
    onSelect: (NavBarItem) -> Unit,
    selectedItem: NavBarItem,
    scrollBehavior: BottomAppBarScrollBehavior
) {
//    var selectedItem: NavBarItem? by remember { mutableStateOf(NavBarItem.Home) }

    BottomAppBar(
        modifier = modifier.fillMaxWidth(),
        scrollBehavior = scrollBehavior
    ) {
        items.forEach { item ->
            Box(
                modifier = Modifier.weight(1f)
                    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                        onSelect(item)
                    },
                contentAlignment = Alignment.Center
            ) {
                NavBarItem(
                    modifier = Modifier, item = item, isSelected = selectedItem == item
                )
            }
        }
    }
}

@Composable
private fun NavBarItem(
    modifier: Modifier = Modifier, item: NavBarItem, isSelected: Boolean
) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onBackground,
            contentDescription = item.title,
            imageVector = if (isSelected) item.selectedImage else item.unselectedImage,
            modifier = Modifier
                .background(
                    color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent,
                    shape = RoundedCornerShape(20.dp) // "Pill" shape
                )
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            item.title,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onBackground,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        )
    }
}
