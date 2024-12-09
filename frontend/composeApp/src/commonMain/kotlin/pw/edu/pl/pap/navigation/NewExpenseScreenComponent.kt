package pw.edu.pl.pap.navigation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import pw.edu.pl.pap.apiclient.ApiClient
import pw.edu.pl.pap.data.NewExpense
import pw.edu.pl.pap.data.User

class NewExpenseScreenComponent(
    componentContext: ComponentContext,
    apiClient: ApiClient,
    coroutineScope: CoroutineScope,
    onBack: () -> Unit
) : BaseExpenseScreenComponent(componentContext, apiClient, coroutineScope, onBack) {

    fun confirm(onConfirm: () -> Unit) {
        // find user
        // save expense
//        val id: Long = 5
        val user = User(1, "Marcinek", "Marcinkowski", "Kaczka2137@gmail.com")
        val date: LocalDate = Clock.System.todayIn(TimeZone.UTC)
//        val category: Category = Category(5, "Test")
//        val expense: Expense = Expense(id, price.value.toFloat(), user, date, category )
        val newExpense = NewExpense(newPrice.value.toFloat(), date, user)

        coroutineScope.launch{
            apiClient.postNewExpense(newExpense)
            onConfirm()
        }

        println("confirmed " + newExpense.price)
    }

}