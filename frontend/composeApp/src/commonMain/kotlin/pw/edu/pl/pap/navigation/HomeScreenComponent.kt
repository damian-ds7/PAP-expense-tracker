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

class HomeScreenComponent(
    componentContext: ComponentContext,
    private val apiClient: ApiClient,
    private val coroutineScope: CoroutineScope,
    val onAddExpenseButtonClicked: () -> Unit,
    val onExpenseClick: (Expense) -> Unit
) : ComponentContext by componentContext {

    sealed class NavigationState {
        data object InitialLoad : NavigationState()
        data object FromNewExpenseScreen : NavigationState()
        data class FromExpenseDetailsScreen(val expense: Expense) : NavigationState()
        data object Empty : NavigationState()
    }

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.InitialLoad)
    val navigationState: StateFlow<NavigationState> get() = _navigationState


    fun updateNavigationState(newState: NavigationState) {
        _navigationState.value = newState
    }

    fun handleNavigationBasedOnState() {
        when (_navigationState.value) {
            is NavigationState.InitialLoad -> {
                fetchHomeInfo()
                fetchAllExpenses()
            }
            is NavigationState.FromNewExpenseScreen -> {
                getRecentExpense()
            }
            is NavigationState.FromExpenseDetailsScreen -> {
                val expense = (_navigationState.value as NavigationState.FromExpenseDetailsScreen).expense
                updateExpense(expense)
            }
            is NavigationState.Empty -> {
                // Do nothing
            }
        }

        updateNavigationState(NavigationState.Empty)
    }

    private val _expensesInfo = MutableStateFlow<TotalExpenses?>(null)
    val expensesInfo: StateFlow<TotalExpenses?> get() = _expensesInfo

    private fun fetchHomeInfo() {
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
    val groupedExpenses: StateFlow<Map<LocalDate, List<Expense>>> get() = _groupedExpenses

    private fun fetchAllExpenses() {
        println("FETCH EXPENSES")
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

    private fun getRecentExpense() {
        println("RECENT EXPENSE")
        coroutineScope.launch {
            try {
                apiClient.getRecentExpense().collect { expense: Expense ->
                    val currentMap = _groupedExpenses.value
                    val currentList = currentMap[expense.date] ?: emptyList()
                    _groupedExpenses.value = currentMap + (expense.date to listOf(expense) + currentList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateExpense(expense: Expense) {
        println("UPDATE EXPENSE")
        coroutineScope.launch {
            try {
                apiClient.getExpense(expense.id).collect { updatedExpense ->
                    val currentMap = _groupedExpenses.value
                    val dateKey = updatedExpense.date
                    val currentList = currentMap[dateKey] ?: emptyList()
                    val updatedList = currentList.map { expense ->
                        if (expense.id == updatedExpense.id) updatedExpense else expense
                    }
                    _groupedExpenses.value = currentMap + (dateKey to updatedList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}