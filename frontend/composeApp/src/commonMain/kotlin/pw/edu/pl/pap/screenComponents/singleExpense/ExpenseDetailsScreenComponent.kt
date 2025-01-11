package pw.edu.pl.pap.screenComponents.singleExpense

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import pw.edu.pl.pap.data.databaseAssociatedData.Expense
import pw.edu.pl.pap.screenComponents.BaseComponent
import pw.edu.pl.pap.util.formatForTextField
import kotlin.math.exp

class ExpenseDetailsScreenComponent(
    baseComponent: BaseComponent,
    onBack: () -> Unit,
    private val expense: Expense
) : BaseExpenseScreenComponent(baseComponent, onBack) {

    override var title: MutableState<String> = mutableStateOf(expense.title)

    override var categoryIndex: MutableState<Int> = mutableStateOf(categories.indexOf(expense.category))

    override var date: MutableState<LocalDate> = mutableStateOf(expense.expenseDate)

    override var price: MutableState<String> = mutableStateOf(formatForTextField(expense.price))

    override var currencyIndex: MutableState<Int> = mutableStateOf(currencies.indexOf(expense.currency))

    override var methodOfPaymentIndex: MutableState<Int> =
        mutableStateOf(methodsOfPayment.indexOf(expense.methodOfPayment))

    override var userIndex: MutableState<Int> = mutableStateOf(users.indexOf(expense.user))

    val hasChanges by derivedStateOf {
        title.value != expense.title ||
                categoryIndex.value != categories.indexOf(expense.category) ||
                date.value != expense.expenseDate ||
                price.value != formatForTextField(expense.price) ||
                currencyIndex.value != currencies.indexOf(expense.currency) ||
                methodOfPaymentIndex.value != methodsOfPayment.indexOf(expense.methodOfPayment) ||
                userIndex.value != users.indexOf(expense.user)
    }

    override fun confirm() {
        val newExpense = expense.copy(
            title = title.value,
            price = price.value.toFloat(),
            user = users[userIndex.value],
            expenseDate = date.value,
            category = categories[categoryIndex.value],
            currency = currencies[currencyIndex.value],
            methodOfPayment = methodsOfPayment[methodOfPaymentIndex.value],
        )

        if (newExpense == expense) {
            onBack()
            return
        }

        coroutineScope.launch {
            expenseRepository.updateExpense(newExpense)
            onBack()
        }
    }

    fun deleteExpense() {
        println("Deleting expense $expense")
        coroutineScope.launch {
            expenseRepository.deleteExpense(expense)
            onBack()
        }
    }
}