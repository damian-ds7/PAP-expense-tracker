package pw.edu.pl.pap.navigation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pw.edu.pl.pap.api.ApiService
import pw.edu.pl.pap.data.*

class HomeScreenComponent(
    componentContext: ComponentContext,
    private val apiService: ApiService,
    private val coroutineScope: CoroutineScope,
    val onAddExpenseButtonClicked: () -> Unit,
    val onExpenseClick: (Expense) -> Unit
) : ComponentContext by componentContext {

    sealed class NavigationState {
        data object InitialLoad : NavigationState()
        data object FromNewExpenseScreen : NavigationState()
        data class FromExpenseDetailsScreenEdit(val expense: Expense) : NavigationState()
        data class FromExpenseDetailsScreenDelete(val expense: Expense) : NavigationState()
        data object Empty : NavigationState()
    }

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.InitialLoad)
    val navigationState: StateFlow<NavigationState> get() = _navigationState


    fun updateNavigationState(newState: NavigationState) {
        _navigationState.value = newState
    }

    fun getDataBasedOnState() {
        when (_navigationState.value) {
            is NavigationState.InitialLoad -> {
                fetchAllExpenses()
            }

            is NavigationState.FromNewExpenseScreen -> {
                getRecentExpense()
            }

            is NavigationState.FromExpenseDetailsScreenEdit -> {
                val expense = (_navigationState.value as NavigationState.FromExpenseDetailsScreenEdit).expense
                updateExpense(expense)
            }

            is NavigationState.FromExpenseDetailsScreenDelete -> {
                val expense = (_navigationState.value as NavigationState.FromExpenseDetailsScreenDelete).expense
                deleteExpense(expense)
            }

            is NavigationState.Empty -> {
                // Do nothing
            }
        }
        fetchHomeInfo()
        updateNavigationState(NavigationState.Empty)
    }

    private val _homeInfo = MutableStateFlow<TotalExpenses?>(null)
    val homeInfo: StateFlow<TotalExpenses?> get() = _homeInfo

    private fun fetchHomeInfo() {
        coroutineScope.launch {
            try {
                val homeData = apiService.expenseApiClient.getTotalExpenses("family", "herkules1@gmail.com")
                _homeInfo.value = homeData
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val _groupedExpenses = MutableStateFlow(ExpenseMap())
    val groupedExpenses: StateFlow<ExpenseMap> get() = _groupedExpenses

    private val _currentGroupKey = MutableStateFlow(GroupKey.DATE)
    val currentGroupKey: StateFlow<GroupKey> get() = _currentGroupKey

    val currentGroupingOrder: StateFlow<Order>
        get() = _groupedExpenses.value.groupingOrder

    fun updateGroupingKey(key: GroupKey) {
        _currentGroupKey.value = key
    }

    private fun currentExpenseMethod(): () -> Flow<ExpenseMap> {
        return when (_currentGroupKey.value) {
            GroupKey.DATE -> apiService.expenseApiClient::getExpenseDateMap
            GroupKey.CATEGORY -> apiService.expenseApiClient::getExpenseCatMap
        }
    }

    private fun fetchAllExpenses() {
//        println("FETCH EXPENSES")
        coroutineScope.launch {
            try {
                val getExpenseMap = currentExpenseMethod()
                getExpenseMap().collect { expenses ->
                    _groupedExpenses.value = expenses
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getRecentExpense() {
//        println("RECENT EXPENSE")
        coroutineScope.launch {
            try {
                apiService.expenseApiClient.getRecentExpense().collect { expense: Expense ->
                    _groupedExpenses.value.addExpense(getCurrentKey(expense), expense)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateExpense(expense: Expense) {
//        println("UPDATE EXPENSE")
        coroutineScope.launch {
            try {
                apiService.expenseApiClient.getExpense(expense.id).collect { updatedExpense ->
                    _groupedExpenses.value.updateExpense(getCurrentKey(updatedExpense), updatedExpense)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun deleteExpense(expense: Expense) {
        _groupedExpenses.value.deleteExpense(getCurrentKey(expense), expense.id)
    }

    private fun getCurrentKey(expense: Expense): GroupMapKey {
        return when (_currentGroupKey.value) {
            GroupKey.DATE -> GroupMapKey.DateKey(expense.date)
            GroupKey.CATEGORY -> GroupMapKey.StringKey(expense.category.name)
        }
    }

    fun sortGroups() {
        _groupedExpenses.value.switchGroupingOrder()
    }
}