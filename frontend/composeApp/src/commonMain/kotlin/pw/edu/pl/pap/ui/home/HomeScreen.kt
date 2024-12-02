package pw.edu.pl.pap.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pw.edu.pl.pap.viewmodel.HomeViewModel
import pw.edu.pl.pap.ui.common.LoadingScreen
import androidx.compose.foundation.lazy.items
import pw.edu.pl.pap.ui.addExpense.NewExpenseScreen
import pw.edu.pl.pap.viewmodel.NewExpenseViewModel
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    var showAddExpenseScreen by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    val homeInfo = viewModel.expensesInfo.collectAsState().value
    val groupedRecords = viewModel.groupedRecords.collectAsState().value

    println(groupedRecords)

    LaunchedEffect(Unit) {
        viewModel.fetchHomeInfo()
        viewModel.fetchRecords()
        isLoading = false
    }

    if (isLoading) {
        LoadingScreen()
    } else if (homeInfo != null && !showAddExpenseScreen) {
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
                GroupedRecordsList(groupedRecords) {}
            }
        }
        PlusButton(showAddExpenseScreen, onUpdate = { showAddExpenseScreen = !showAddExpenseScreen })
    } else if (showAddExpenseScreen) {
        NewExpenseScreen(
            viewModel = NewExpenseViewModel(viewModel.passApiClient()),
            onClose = { showAddExpenseScreen = false })
    } else {
        Text(
            text = "No data available",
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
    }
}
