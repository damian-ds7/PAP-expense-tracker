package pw.edu.pl.pap.screenComponents.singleExpense

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import pw.edu.pl.pap.data.databaseAssociatedData.Expense
import pw.edu.pl.pap.screenComponents.BaseComponent
import pw.edu.pl.pap.util.formatForTextField

class ExpenseDetailsScreenComponent(
    baseComponent: BaseComponent,
    onBack: () -> Unit,
    private val expense: Expense
) : BaseExpenseScreenComponent(baseComponent, onBack) {

    override var title: MutableState<String> = mutableStateOf(expense.title)

    override var categoryIndex: MutableState<Int> = mutableStateOf(expense.category.id.toInt() - 1)

    override var date: MutableState<LocalDate> = mutableStateOf(expense.expenseDate)

    override var price: MutableState<String> = mutableStateOf(formatForTextField(expense.price))

    override var currencyIndex: MutableState<Int> = mutableStateOf(expense.currency.id.toInt() - 1)

    //TODO when no given currency in list there's an out of bounds error for index -1
    override var methodOfPaymentIndex: MutableState<Int> =
        mutableStateOf(methodsOfPayment.indexOf(expense.methodOfPayment.name))

    override var userIndex: MutableState<Int> = mutableStateOf(expense.user.id.toInt())

    val noChange by derivedStateOf { canConfirm && price.value == formatForTextField(expense.price) }

    init {
        setupInputFields()
    }

    override fun confirm() {
        val newExpense = expense.copy(
            title = title.value,
            price = price.value.toFloat(),
            expenseDate = date.value,
        )

        if (newExpense == expense) {
            onBack()
            return
        }

        coroutineScope.launch {
            expenseRepository.updateExpense(newExpense)
            onBack()
        }

        println("Updated Expense ${newExpense.id} from ${expense.price} to ${newExpense.price}")
    }

    fun deleteExpense() {
        println("Deleting expense $expense")
        coroutineScope.launch {
            expenseRepository.deleteExpense(expense)
            onBack()
        }
    }
}