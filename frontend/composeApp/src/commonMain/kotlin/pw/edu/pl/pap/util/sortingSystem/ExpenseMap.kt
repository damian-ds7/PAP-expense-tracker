package pw.edu.pl.pap.util.sortingSystem

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pw.edu.pl.pap.data.databaseAssociatedData.Expense
import java.util.*

class ExpenseMap(
    comparator: Comparator<in GroupMapKey> = naturalOrder(),
    initialGroupingOrder: Order = Order.ASCENDING,
) : TreeMap<GroupMapKey, List<Expense>>(comparator) {

    private val _groupingOrder = MutableStateFlow(initialGroupingOrder)
    val groupingOrder = _groupingOrder.asStateFlow()

    private var updated: Boolean = false

    fun switchGroupingOrder() {
        _groupingOrder.value = if (_groupingOrder.value == Order.ASCENDING) Order.DESCENDING else Order.ASCENDING
    }

    fun addExpense(key: GroupMapKey, expense: Expense) {
        val expenseList = this[key]?.toMutableList()?.apply {
            add(0, expense)
        } ?: mutableListOf(expense)
        this[key] = expenseList.toList()
        updated = true
    }

    fun deleteExpense(key: GroupMapKey, expenseId: Long) {
        val expenseList = this[key] ?: return
        val updatedList = expenseList.filter { it.id != expenseId }
        if (updatedList.isEmpty()) {
            this.remove(key)
        } else {
            this[key] = updatedList
        }
        updated = true
    }

    fun updateExpense(key: GroupMapKey, updatedExpense: Expense) {
        val expenseList = this[key] ?: return
        val updatedList = expenseList.map {
            if (it.id == updatedExpense.id) updatedExpense else it
        }
        this[key] = updatedList
        updated = true
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

    override fun equals(other: Any?): Boolean {
        if (!updated) {
            return super.equals(other)
        } else {
            updated = false
            return false
        }
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}

inline fun ExpenseMap.forEachList(action: (Map.Entry<GroupMapKey, List<Expense>>) -> Unit) {
    if (this.groupingOrder.value == Order.DESCENDING) {
        for (element in this.descendingMap()) action(element)
    } else {
        for (element in this) action(element)
    }
}