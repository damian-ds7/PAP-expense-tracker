package pw.edu.pl.pap.repositories.data

import io.ktor.http.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pw.edu.pl.pap.api.data.ExpenseApi
import pw.edu.pl.pap.data.databaseAssociatedData.*
import pw.edu.pl.pap.util.sortingSystem.*

class ExpenseRepository(val api: ExpenseApi) {

    private val _homeInfo = MutableStateFlow(TotalExpenses())
    val homeInfo: StateFlow<TotalExpenses> get() = _homeInfo

    private val _groupedExpenses = MutableStateFlow(ExpenseMap())
    val groupedExpenses: StateFlow<ExpenseMap> get() = _groupedExpenses

    private val _moreToLoad = MutableStateFlow(true)
    val moreToLoad: StateFlow<Boolean> get() = _moreToLoad

    private val _currentGroupingKey = MutableStateFlow(GroupKey.DATE)
    val currentGroupingKey: StateFlow<GroupKey> get() = _currentGroupingKey

    fun updateGroupingKey(key: GroupKey) {
        _currentGroupingKey.value = key
        when (key) {
            GroupKey.DATE -> setGroupingOrder(Order.DESCENDING)
            GroupKey.CATEGORY -> setGroupingOrder(Order.ASCENDING)
        }
    }

    private var nextPageInfo = NextExpensePageInfo(order = Order.DESCENDING.paramName)
    private val _currentGroupingOrder = MutableStateFlow(Order.DESCENDING)
    val currentGroupingOrder: StateFlow<Order> get() = _currentGroupingOrder

    fun switchGroupingOrder() {
        val newOrder =
            if (_currentGroupingOrder.value == Order.ASCENDING) Order.DESCENDING else Order.ASCENDING
        setGroupingOrder(newOrder)
    }

    private fun setGroupingOrder(newOrder: Order) {
        _currentGroupingOrder.value = newOrder
        nextPageInfo = NextExpensePageInfo(order = newOrder.paramName)
    }

    suspend fun getTotalExpenses(group: String) {
        try {
            _homeInfo.value = api.getTotalExpenses(group)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun loadPage(group: String): ExpenseMap {
        val page = when (_currentGroupingKey.value) {
            GroupKey.DATE -> getExpenseDateMap(group)
            GroupKey.CATEGORY -> getExpenseCatMap(group)
        }

        val comparator = when (_currentGroupingOrder.value) {
            Order.ASCENDING -> naturalOrder<GroupMapKey>()
            Order.DESCENDING -> reverseOrder()
        }

        nextPageInfo = nextPageInfo.copy(lastId = page.nextLastId, lastKey = page.nextLastKey)
        _moreToLoad.value = page.hasMore
        val newMap = page.data.toMap(ExpenseMap(comparator = comparator))
        return newMap
    }

    suspend fun loadInitialPage(group: String) {
        nextPageInfo = NextExpensePageInfo(order = nextPageInfo.order)
        try {
            val newMap = loadPage(group)
            _groupedExpenses.value = newMap
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun loadNextPage(group: String) {
        try {
            val nextMap = _groupedExpenses.value
            nextMap.add(loadPage(group))
            _groupedExpenses.value = nextMap
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private suspend fun getExpenseDateMap(group: String): DateKeyExpensePage {
        return api.getExpenseDateMap(group, nextPageInfo.toMap())
    }

    private suspend fun getExpenseCatMap(group: String): StringKeyExpensePage {
        return api.getExpenseCatMap(group, nextPageInfo.toMap())
    }

    suspend fun updateExpense(expense: Expense) {
        try {
            api.updateExpense(expense.id, expense)
            val newMap = _groupedExpenses.value
            val updatedExpense = api.getExpense(expense.id)
            newMap.updateExpense(getKeyFromExpense(updatedExpense), updatedExpense, getKeyFromExpense(expense))
            _groupedExpenses.value = newMap
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun addExpense(expense: NewExpense) {
        try {
            api.postNewExpense(expense)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getRecentExpense(group: String) {
        try {
            val recentExpense = api.getRecentExpense(group)
            val newMap = _groupedExpenses.value
            newMap.addExpense(getKeyFromExpense(recentExpense), recentExpense)
            _groupedExpenses.value = newMap
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteExpense(expense: Expense) {
        if (api.deleteExpense(expense.id).status.isSuccess()) {
            _groupedExpenses.value.deleteExpense(getKeyFromExpense(expense), expense.id)
        }
    }

    private fun getKeyFromExpense(expense: Expense): GroupMapKey {
        return when (_currentGroupingKey.value) {
            GroupKey.DATE -> GroupMapKey.DateKey(expense.expenseDate)
            GroupKey.CATEGORY -> GroupMapKey.StringKey(expense.category.name)
        }
    }

    private fun getDefaultOrder(): Order {
        return when (_currentGroupingKey.value) {
            GroupKey.DATE -> Order.DESCENDING
            GroupKey.CATEGORY -> Order.ASCENDING
        }
    }

}