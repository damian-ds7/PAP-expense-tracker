package pw.edu.pl.pap.screenComponents.mainScreens

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pw.edu.pl.pap.data.databaseAssociatedData.Expense
import pw.edu.pl.pap.data.databaseAssociatedData.TotalExpenses
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.util.sortingSystem.ExpenseMap
import pw.edu.pl.pap.util.sortingSystem.GroupKey
import pw.edu.pl.pap.util.sortingSystem.GroupMapKey
import pw.edu.pl.pap.util.sortingSystem.Order

class HomeScreenComponent(
    baseScreenComponent: BaseScreenComponent,
    val onAddExpenseButtonClicked: () -> Unit,
    val onExpenseClick: (Expense) -> Unit
) : BaseScreenComponent by baseScreenComponent {

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
        updateNavigationState(NavigationState.Empty)
    }

    private val _userGroupInfo = MutableStateFlow<List<UserGroup>?>(emptyList())
    val userGroupInfo: StateFlow<List<UserGroup>?> get() = _userGroupInfo

    private val _currentUserGroup = MutableStateFlow<UserGroup?>(null)
    val currentUserGroup: StateFlow<UserGroup?> get() = _currentUserGroup

    fun updateUserGroup(key: UserGroup) {
        _currentUserGroup.value = key
    }

    private val _homeInfo = MutableStateFlow<TotalExpenses?>(null)
    val homeInfo: StateFlow<TotalExpenses?> get() = _homeInfo

    fun fetchHomeInfo() {
        runBlocking {
            try {
                val userGroupInfo = apiService.groupApiClient.getUserGroups()
                _userGroupInfo.value = userGroupInfo
                _currentUserGroup.value = _userGroupInfo.value?.first()
                val homeData = apiService.expenseApiClient.getTotalExpensesForGroup(currentUserGroup.value?.name)
                _homeInfo.value = homeData.first()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val _groupedExpenses = MutableStateFlow(ExpenseMap())
    val groupedExpenses: StateFlow<ExpenseMap> get() = _groupedExpenses

    private val _currentGroupingKey = MutableStateFlow(GroupKey.DATE)
    val currentGroupingKey: StateFlow<GroupKey> get() = _currentGroupingKey

    val currentGroupingOrder: StateFlow<Order>
        get() = _groupedExpenses.value.groupingOrder

    fun updateGroupingKey(key: GroupKey) {
        _currentGroupingKey.value = key
    }

    private fun currentExpenseMethod(): () -> Flow<ExpenseMap> {
        return when (_currentGroupingKey.value) {
            GroupKey.DATE -> { { apiService.expenseApiClient.getExpenseDateMapForGroup(_currentUserGroup.value?.name) } }
            GroupKey.CATEGORY -> { { apiService.expenseApiClient.getExpenseCatMapForGroup(_currentUserGroup.value?.name) } }
        }
    }

    private fun fetchAllExpenses() {
        println("FETCH EXPENSES")
        runBlocking {
            try {
                val getExpenseMap = currentExpenseMethod()
                getExpenseMap().collect { expenses ->
                    _groupedExpenses.value = expenses
                    println(expenses.groupingOrder.value)
                    println(_groupedExpenses.value.groupingOrder.value)
                    println(currentGroupingOrder.value)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getRecentExpense() {
//        println("RECENT EXPENSE")
        runBlocking {
            try {
                apiService.expenseApiClient.getRecentExpense().collect { expense: Expense ->
                    _groupedExpenses.value.addExpense(getCurrentKey(expense), expense)
//                    _groupedExpenses.value = _groupedExpenses.value
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateExpense(expense: Expense) {
//        println("UPDATE EXPENSE")
        runBlocking {
            try {
                apiService.expenseApiClient.getExpense(expense.id).collect { updatedExpense ->
                    _groupedExpenses.value.updateExpense(getCurrentKey(updatedExpense), updatedExpense)
                    _groupedExpenses.value = _groupedExpenses.value
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun deleteExpense(expense: Expense) {
        _groupedExpenses.value.deleteExpense(getCurrentKey(expense), expense.id)
        _groupedExpenses.value = _groupedExpenses.value
    }

    private fun getCurrentKey(expense: Expense): GroupMapKey {
        return when (_currentGroupingKey.value) {
            GroupKey.DATE -> GroupMapKey.DateKey(expense.date)
            GroupKey.CATEGORY -> GroupMapKey.StringKey(expense.category.name)
        }
    }

    fun sortGroups() {
        coroutineScope.launch {
            _groupedExpenses.value.switchGroupingOrder()
        }
    }
}