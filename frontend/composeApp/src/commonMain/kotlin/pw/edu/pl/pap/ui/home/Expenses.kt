package pw.edu.pl.pap.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate
import pw.edu.pl.pap.data.Expense
import pw.edu.pl.pap.util.formatDate
import pw.edu.pl.pap.util.formatNumber


@Composable
fun ExpenseBlock(expense: Expense, onClick: (Expense) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick(expense) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
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
                text = "${formatNumber(expense.price)} zł",
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun DateHeader(date: LocalDate) {
    Text(
        text = formatDate(date, "dd MMMM yyyy"),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun GroupedExpensesList(
    groupedExpenses: Map<LocalDate, List<Expense>>,
    onExpenseClick: (Expense) -> Unit
) {
    groupedExpenses.forEach { (date, expenseForDate) ->
        DateHeader(date)

        expenseForDate.forEach { expense ->
            ExpenseBlock(expense, onClick = onExpenseClick)
        }
    }
}