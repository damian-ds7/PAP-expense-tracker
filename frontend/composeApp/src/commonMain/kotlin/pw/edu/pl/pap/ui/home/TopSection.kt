package pw.edu.pl.pap.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pw.edu.pl.pap.navigation.HomeScreenComponent
import pw.edu.pl.pap.util.formatForDisplay

@Composable
fun TopSection(component: HomeScreenComponent) {
    val homeInfo by component.homeInfo.collectAsState()

    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "My Expenses",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${formatForDisplay(homeInfo?.userExpenses)} zł",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Total Expenses",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${formatForDisplay(homeInfo?.groupExpenses)} zł",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}