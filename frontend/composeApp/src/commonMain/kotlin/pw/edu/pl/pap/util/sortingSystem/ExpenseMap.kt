package pw.edu.pl.pap.util.sortingSystem

import pw.edu.pl.pap.data.databaseAssociatedData.Expense
import java.util.*

class ExpenseMap(
    comparator: Comparator<in GroupMapKey> = naturalOrder(),
) : TreeMap<GroupMapKey, List<Expense>>(comparator) {

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

    fun updateExpense(key: GroupMapKey, updatedExpense: Expense, oldKey: GroupMapKey) {
        if (key != oldKey) {
            val oldExpenseList = this[oldKey]?.toMutableList() ?: return
            oldExpenseList.removeIf { it.id == updatedExpense.id }
            if (oldExpenseList.isEmpty()) {
                this.remove(oldKey)
            } else {
                this[oldKey] = oldExpenseList
            }
        }

        val expenseList = this[key] ?: return
        val updatedList = expenseList.map {
            if (it.id == updatedExpense.id) updatedExpense else it
        }
        this[key] = updatedList
    }
}

fun <K, V> MutableMap<K, List<V>>.add(other: Map<K, List<V>>) {
    other.forEach { (key, value) ->
        this[key] = this[key]?.union(value)?.toList() ?: value
    }
}
