package pw.edu.pl.pap.navigation.singleExpense

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import pw.edu.pl.pap.api.ApiService
import pw.edu.pl.pap.data.databaseAssociatedData.NewExpense
import pw.edu.pl.pap.data.databaseAssociatedData.User

class NewExpenseScreenComponent(
    componentContext: ComponentContext,
    apiService: ApiService,
    coroutineScope: CoroutineScope,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) : BaseExpenseScreenComponent(componentContext, apiService, coroutineScope, onDismiss, onSave) {

    override fun confirm() {
        // find user
        // save expense
//        val id: Long = 5
        val user = User(1, "Marcinek", "Marcinkowski", "Kaczka2137@gmail.com")
        val date: LocalDate = Clock.System.todayIn(TimeZone.UTC)
//        val category: Category = Category(5, "Test")
//        val expense: Expense = Expense(id, price.value.toFloat(), user, date, category )
        val newExpense = NewExpense(newPrice.value.toFloat(), date, user)

        coroutineScope.launch{
            apiService.expenseApiClient.postNewExpense(newExpense)
            onSave()
        }

        println("confirmed " + newExpense.price)
    }

}