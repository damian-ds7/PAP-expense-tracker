package pw.edu.pl.pap.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

class ExpenseMap(
    comparator: Comparator<in GroupMapKey> = naturalOrder(),
    initialGroupingOrder: Order = Order.ASCENDING,
) : TreeMap<GroupMapKey, List<Expense>>(comparator) {

    private val _groupingOrder = MutableStateFlow(initialGroupingOrder)
    val groupingOrder = _groupingOrder.asStateFlow()

    fun switchGroupingOrder() {
        _groupingOrder.value = if (_groupingOrder.value == Order.ASCENDING) Order.DESCENDING else Order.ASCENDING
    }

    fun addExpense(key: GroupMapKey, expense: Expense) {
        val expenseList = this[key]?.toMutableList()?.apply {
            add(0, expense)
        } ?: mutableListOf(expense)
        this[key] = expenseList.toList()
    }

    fun deleteExpense(key: GroupMapKey, expenseId: Long) {
        val expenseList = this[key] ?: return
        val updatedList = expenseList.filter { it.id != expenseId }
        if (updatedList.isEmpty()) {
            this.remove(key)
        } else {
            this[key] = updatedList
        }
    }

    fun updateExpense(key: GroupMapKey, updatedExpense: Expense) {
        val expenseList = this[key] ?: return
        val updatedList = expenseList.map {
            if (it.id == updatedExpense.id) updatedExpense else it
        }
        this[key] = updatedList
    }

    fun <T : Comparable<T>> sortEachList(keySelector: (Expense) -> T, order: Order) {
        for ((mapKey, expenseList) in this) {
            val sortedList = when (order) {
                Order.ASCENDING -> expenseList.sortedBy(keySelector)
                Order.DESCENDING -> expenseList.sortedByDescending(keySelector)
            }
            this[mapKey] = sortedList.toMutableList()
        }
    }
}

inline fun ExpenseMap.forEachList(action: (Map.Entry<GroupMapKey, List<Expense>>) -> Unit) {
    if (this.groupingOrder.value == Order.DESCENDING) {
        for (element in this.descendingMap()) action(element)
    } else {
        for (element in this) action(element)
    }
}