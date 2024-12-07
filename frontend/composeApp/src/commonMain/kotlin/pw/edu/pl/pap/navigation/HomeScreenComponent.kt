package pw.edu.pl.pap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewModelScope
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import pw.edu.pl.pap.apiclient.ApiClient
import pw.edu.pl.pap.data.Record
import pw.edu.pl.pap.data.TotalExpenses
import pw.edu.pl.pap.navigation.RootComponent.Child
import pw.edu.pl.pap.navigation.RootComponent.Configuration

class HomeScreenComponent (
    componentContext: ComponentContext,
    private val apiClient: ApiClient,
    private val coroutineScope: CoroutineScope,
    val onAddExpenseButtonClicked: () -> Unit
) : ComponentContext by componentContext {

    private val _expensesInfo = MutableStateFlow<TotalExpenses?>(null)
    val expensesInfo: StateFlow<TotalExpenses?> = _expensesInfo

    fun fetchHomeInfo() {
        coroutineScope.launch {
            try {
                val homeData = apiClient.getTotalExpenses("family", "herkules1@gmail.com")
                _expensesInfo.value = homeData
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val _groupedRecords = MutableStateFlow<Map<LocalDate, List<Record>>>(emptyMap())
    val groupedRecords: StateFlow<Map<LocalDate, List<Record>>> = _groupedRecords

    fun fetchRecords() {
        coroutineScope.launch {
            try {
                apiClient.getRecords().collect { records ->
                    _groupedRecords.value = records
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateRecentRecord() {
        coroutineScope.launch {
            try {
                apiClient.getRecentRecord().collect { record: Record ->
                    val currentMap = _groupedRecords.value
                    val currentList = currentMap[record.date] ?: emptyList()
                    _groupedRecords.value = currentMap + (record.date to currentList + record)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



}