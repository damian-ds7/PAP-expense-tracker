package pw.edu.pl.pap.navigation

import androidx.compose.runtime.*
import com.arkivanov.decompose.ComponentContext
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pw.edu.pl.pap.apiclient.ApiClient
import pw.edu.pl.pap.data.Expense

class ExpenseDetailsScreenComponent(
    componentContext: ComponentContext,
    apiClient: ApiClient,
    coroutineScope: CoroutineScope,
    private val expense: Expense,
    onBack: () -> Unit
) : BaseExpenseScreenComponent(componentContext, apiClient, coroutineScope, onBack) {

    override var newPrice: MutableState<String> = mutableStateOf("${expense.price}")

    override var selectedIndex: MutableState<Int> = mutableStateOf(0)

    val noChange by derivedStateOf { canConfirm && newPrice.value.toFloat() == expense.price }

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