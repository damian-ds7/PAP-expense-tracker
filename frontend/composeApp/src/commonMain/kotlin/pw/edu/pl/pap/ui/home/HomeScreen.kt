package pw.edu.pl.pap.ui.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pw.edu.pl.pap.navigation.HomeScreenComponent
import pw.edu.pl.pap.ui.common.LoadingScreen

@Composable
fun HomeScreen(component: HomeScreenComponent) {
    var isLoading by remember { mutableStateOf(true) }
    val homeInfo = component.expensesInfo.collectAsState().value
    val groupedExpenses = component.groupedExpenses.collectAsState().value


    LaunchedEffect(Unit) {
        component.fetchHomeInfo()
        component.fetchExpenses()
        isLoading = false
    }

    if (isLoading) {
        LoadingScreen()
    } else if (homeInfo != null) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            item {
                TopSection(homeInfo)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                GroupedExpensesList(groupedExpenses, onExpenseClick = { expense ->
                    component.onExpenseClick(expense)
                })
            }
        }
        PlusButton(onUpdate = { component.onAddExpenseButtonClicked() })
    } else {
        Text(
            text = "No data available",
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
    }
}
