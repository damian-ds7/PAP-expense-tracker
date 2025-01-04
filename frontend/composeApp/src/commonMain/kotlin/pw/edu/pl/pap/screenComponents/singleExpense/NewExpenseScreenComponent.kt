package pw.edu.pl.pap.screenComponents.singleExpense

import kotlinx.coroutines.runBlocking
import pw.edu.pl.pap.data.databaseAssociatedData.NewExpense
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.screenComponents.BaseScreenComponent

class NewExpenseScreenComponent(
    baseComponent: BaseScreenComponent,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    private val currentUserGroup: UserGroup,
) : BaseExpenseScreenComponent(baseComponent, onDismiss, onSave) {


    override fun confirm() {
        val newExpense = NewExpense(
            title = title.value,
            price = price.value.toFloat(),
            user = users[userIndex.value],
            groupName = currentUserGroup.name,
            categoryName = categories[categoryIndex.value],
            expenseDate = date.value,
            methodOfPayment = methodsOfPayment[methodOfPaymentIndex.value],
            currencyCode = currencies[currencyIndex.value]
        )

        runBlocking{
            apiService.expenseApiClient.postNewExpense(newExpense)
            onSave()
        }
    }
}