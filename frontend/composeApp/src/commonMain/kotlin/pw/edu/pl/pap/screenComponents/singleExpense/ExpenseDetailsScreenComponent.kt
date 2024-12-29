package pw.edu.pl.pap.screenComponents.singleExpense

import androidx.compose.runtime.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import pw.edu.pl.pap.data.databaseAssociatedData.Expense
import pw.edu.pl.pap.screenComponents.mainScreens.BaseScreenComponent
import pw.edu.pl.pap.util.formatForTextField

class ExpenseDetailsScreenComponent(
    baseComponent: BaseScreenComponent,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    private val onDelete: () -> Unit,
    private val expense: Expense
    ) : BaseExpenseScreenComponent(baseComponent, onDismiss, onSave) {

    override var title: MutableState<String> = mutableStateOf("")
    //TODO fetch title
    override var categoryIndex: MutableState<Int> = mutableStateOf(expense.category.id.toInt() - 1)

    override var date: MutableState<LocalDate> = mutableStateOf(expense.date)

    override var newPrice: MutableState<String> = mutableStateOf(formatForTextField(expense.price))
    override var currencyIndex: MutableState<Int> = mutableStateOf(expense.currency.id.toInt() - 1)
    //TODO fetch currency
    override var methodOfPaymentIndex: MutableState<Int> = mutableStateOf(0)
    //TODO fetch method of payment

    override var userIndex: MutableState<Int> = mutableStateOf(expense.user.id.toInt())

    val noChange by derivedStateOf { canConfirm && newPrice.value == formatForTextField(expense.price) }

    override fun confirm() {
        val newExpense = expense.copy(price = newPrice.value.toFloat())

        if (newExpense == expense) {
            onDismiss()
            return
        }

        coroutineScope.launch {
            if (apiService.expenseApiClient.updateExpense(newExpense).status.isSuccess()) {
                onSave()
            }
        }

        println("Updated Expense ${newExpense.id} from ${expense.price} to ${newExpense.price}")
    }
    
    fun deleteExpense() {
        println("Deleting expense $expense")
        coroutineScope.launch { 
            if (apiService.expenseApiClient.deleteExpense(expense.id).status.isSuccess()) {
                onDelete()
            }
        }
    }
}