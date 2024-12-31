package pw.edu.pl.pap.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pw.edu.pl.pap.data.databaseAssociatedData.Expense
import pw.edu.pl.pap.util.sortingSystem.forEachList
import pw.edu.pl.pap.screenComponents.mainScreens.HomeScreenComponent
import pw.edu.pl.pap.util.constants.horizontalPadding
import pw.edu.pl.pap.util.constants.verticalPadding
import pw.edu.pl.pap.util.formatForDisplay


@Composable
fun ExpenseBlock(expense: Expense, onClick: (Expense) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
            .height(50.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick(expense) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = expense.category.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = expense.user.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )
            }

            Text(
                text = "${formatForDisplay(expense.price)} zł",
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun Header(key: String) {
    Text(
        text = key,
        modifier = Modifier
            .fillMaxWidth()
            .padding(verticalPadding),
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun GroupedExpensesList(
    component: HomeScreenComponent,
    onExpenseClick: (Expense) -> Unit
) {
    val groupedExpenses by component.groupedExpenses.collectAsState()
    val order by groupedExpenses.groupingOrder.collectAsState()

    key(order) {
        groupedExpenses.forEachList { (key, expenseList) ->
            Header(key.asString())

            expenseList.forEach { expense ->
                ExpenseBlock(expense, onClick = onExpenseClick)
            }
        }
    }
}