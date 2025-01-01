package pw.edu.pl.pap.screenComponents.singleExpense

import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import pw.edu.pl.pap.data.databaseAssociatedData.NewExpense
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.screenComponents.mainScreens.BaseScreenComponent

class NewExpenseScreenComponent(
    baseComponent: BaseScreenComponent,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) : BaseExpenseScreenComponent(baseComponent, onDismiss, onSave) {

    override fun confirm() {
        // find user
        // save expense
//        val id: Long = 5
        val user = User(1, "Marcinek", "Marcinkowski", "Kaczka2137@gmail.com")
        val date: LocalDate = Clock.System.todayIn(TimeZone.UTC)
//        val category: Category = Category(5, "Test")
//        val expense: Expense = Expense(id, price.value.toFloat(), user, date, category )
        val newExpense = NewExpense(title.value, newPrice.value.toFloat(), date, user)

        coroutineScope.launch{
            apiService.expenseApiClient.postNewExpense(newExpense)
            onSave()
        }

        println("confirmed " + newExpense.price)
    }

}