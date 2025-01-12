package pw.edu.pl.pap.ui.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import pw.edu.pl.pap.screenComponents.mainScreens.HomeScreenComponent
import pw.edu.pl.pap.ui.home.ExpenseBlock

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PaginatedLazyColumn(
    component: HomeScreenComponent,
    listState: LazyListState,
    buffer: Int = 2,
) {
    val isLoading by component.loadingData.collectAsState()

    val shouldLoadMore = remember {
        derivedStateOf {
            listState.reachedBottom(buffer) && !isLoading && component.moreToLoad.value
        }
    }
    val groupedExpenses by component.groupedExpenses.collectAsState()

    LaunchedEffect(listState) {
        snapshotFlow { shouldLoadMore.value }.distinctUntilChanged().filter { it }.collect {
                component.fetchNextPage()
            }
    }
    LazyColumn(
        state = listState
    ) {
        groupedExpenses.forEach { (key, expenseList) ->
            stickyHeader {
                Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                    pw.edu.pl.pap.ui.home.Header(key.asString())
                }
            }

            items(items = expenseList, key = { expense -> expense.id }) { expense ->
                ExpenseBlock(expense, onClick = component.onExpenseClick)
            }
        }

        if (isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }


    }
}

internal fun LazyListState.reachedBottom(buffer: Int = 1): Boolean {
    val lastVisibleItem = this.layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index != null && lastVisibleItem.index >= this.layoutInfo.totalItemsCount - buffer
}