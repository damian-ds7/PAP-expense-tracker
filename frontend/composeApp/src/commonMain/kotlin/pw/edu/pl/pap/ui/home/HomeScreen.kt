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
import pw.edu.pl.pap.data.NewExpense
import pw.edu.pl.pap.navigation.HomeScreenComponent

@Composable
fun HomeScreen(component: HomeScreenComponent) {
    var isLoading by remember { mutableStateOf(true) }
    val homeInfo = component.expensesInfo.collectAsState().value
    val groupedRecords = component.groupedRecords.collectAsState().value


//    var homeInfo by remember { mutableStateOf(component.expensesInfo.collectAsState().value)}
//    var groupedRecords by remember { mutableStateOf(component.groupedRecords.collectAsState().value)}


    LaunchedEffect(Unit) {
        component.fetchHomeInfo()
        component.fetchRecords()
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
                GroupedRecordsList(groupedRecords) {}
            }
        }
        PlusButton(onUpdate = { component.onAddExpenseButtonClicked() } )
    } else {
        Text(
            text = "No data available",
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
    }
}
