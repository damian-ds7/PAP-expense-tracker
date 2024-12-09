package pw.edu.pl.pap.navigation

import androidx.compose.runtime.*
import com.arkivanov.decompose.ComponentContext
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pw.edu.pl.pap.apiclient.ApiClient
import pw.edu.pl.pap.data.Expense
import pw.edu.pl.pap.util.formatForTextField

class ExpenseDetailsScreenComponent(
    componentContext: ComponentContext,
    apiClient: ApiClient,
    coroutineScope: CoroutineScope,
    private val expense: Expense,
    onBack: () -> Unit
) : BaseExpenseScreenComponent(componentContext, apiClient, coroutineScope, onBack) {

    override var title: MutableState<String> = mutableStateOf("")
    //TODO fetch title
    override var categoryIndex: MutableState<Int> = mutableStateOf(expense.category.id.toInt())

    override var date: MutableState<String> = mutableStateOf(expense.date.toString())

    override var newPrice: MutableState<String> = mutableStateOf(formatForTextField(expense.price))
    override var currencyIndex: MutableState<Int> = mutableStateOf(0)
    //TODO fetch currency
    override var methodOfPaymentIndex: MutableState<Int> = mutableStateOf(0)
    //TODO fetch method of payment

    override var userIndex: MutableState<Int> = mutableStateOf(expense.user.id.toInt())

    val noChange by derivedStateOf { canConfirm && newPrice.value == formatForTextField(expense.price) }

    override fun confirm() {
        val newExpense = expense.copy(price = newPrice.value.toFloat())

        if (newExpense == expense) {
            onBack()
            return
        }

        coroutineScope.launch {
            if (apiClient.updateExpense(newExpense).status.isSuccess()) {
                onBack()
            }
        }

        println("Updated Expense ${newExpense.id} from ${expense.price} to ${newExpense.price}")
    }
}