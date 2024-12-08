package pw.edu.pl.pap.navigation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import pw.edu.pl.pap.apiclient.ApiClient
import pw.edu.pl.pap.data.Expense
import pw.edu.pl.pap.data.TotalExpenses

class HomeScreenComponent (
    componentContext: ComponentContext,
    private val apiClient: ApiClient,
    private val coroutineScope: CoroutineScope,
    val onAddExpenseButtonClicked: () -> Unit,
    val onExpenseClick: (Expense) -> Unit
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

    private val _groupedExpenses = MutableStateFlow<Map<LocalDate, List<Expense>>>(emptyMap())
    val groupedExpenses: StateFlow<Map<LocalDate, List<Expense>>> = _groupedExpenses

    fun fetchExpenses() {
        coroutineScope.launch {
            try {
                apiClient.getAllExpenses().collect { expenses ->
                    _groupedExpenses.value = expenses
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateRecentExpense() {
        coroutineScope.launch {
            try {
                apiClient.getRecentExpense().collect { expense: Expense ->
                    val currentMap = _groupedExpenses.value
                    val currentList = currentMap[expense.date] ?: emptyList()
                    _groupedExpenses.value = currentMap + (expense.date to currentList + expense)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



}