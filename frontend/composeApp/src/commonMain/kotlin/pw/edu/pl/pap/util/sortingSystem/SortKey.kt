package pw.edu.pl.pap.util.sortingSystem

import pw.edu.pl.pap.data.databaseAssociatedData.Expense
import kotlin.reflect.KProperty1

enum class SortKey(val property: KProperty1<Expense, *>, val displayName: String) {
    PRICE(Expense::price, "Price"),
    DATE(Expense::expenseDate, "Date")
}