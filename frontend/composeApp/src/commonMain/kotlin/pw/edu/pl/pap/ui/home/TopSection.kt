package pw.edu.pl.pap.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import pw.edu.pl.pap.screenComponents.mainScreens.HomeScreenComponent
import pw.edu.pl.pap.ui.home.sortingSystem.ButtonRow
import pw.edu.pl.pap.util.formatForDisplay

@Composable
fun TopSection(component: HomeScreenComponent, onGroupKeyClick: () -> Unit, onUserGroupClick: () -> Unit) {
    val homeInfo by component.homeInfo.collectAsState()
    val preferences by component.preferences.collectAsState()

    Column {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "My Expenses", fontSize = 20.sp, fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${formatForDisplay(homeInfo.userExpenses)} ${preferences?.currencySymbol ?: ""}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Total Expenses", fontSize = 20.sp, fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${formatForDisplay(homeInfo.groupExpenses)} ${preferences?.currencySymbol ?: ""}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        ButtonRow(
            component = component, onGroupKeyClick = onGroupKeyClick, onUserGroupClick = onUserGroupClick
        )

    }
}