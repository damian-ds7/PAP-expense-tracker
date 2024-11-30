package pw.edu.pl.pap.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pw.edu.pl.pap.viewmodel.HomeViewModel
import pw.edu.pl.pap.ui.common.LoadingScreen
import androidx.compose.foundation.lazy.items

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    var isLoading by remember { mutableStateOf(true) }
    val homeInfo = viewModel.expensesInfo.collectAsState().value
    val records = viewModel.records.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.fetchHomeInfo()
        viewModel.fetchRecords()
        isLoading = false
    }

    if (isLoading) {
        LoadingScreen()
    } else if (homeInfo != null) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            TopSection(homeInfo)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(records) { record ->
                    RecordBlock(record)
                }
            }
        }
    }
}