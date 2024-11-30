package pw.edu.pl.pap.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pw.edu.pl.pap.data.InitialExpenses

@Composable
fun TopSection(homeInfo: InitialExpenses) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "My Expenses",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${homeInfo.userExpenses} zł",
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
                text = "${homeInfo.expenses} zł",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}