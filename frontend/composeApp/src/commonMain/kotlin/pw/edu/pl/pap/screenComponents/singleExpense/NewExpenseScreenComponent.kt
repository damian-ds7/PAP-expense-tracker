package pw.edu.pl.pap.screenComponents.singleExpense

import kotlinx.coroutines.launch
import pw.edu.pl.pap.data.databaseAssociatedData.NewExpense
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.screenComponents.BaseComponent

class NewExpenseScreenComponent(
    baseComponent: BaseComponent,
    onBack: () -> Unit,
    private val currentUserGroup: UserGroup,
) : BaseExpenseScreenComponent(baseComponent, onBack) {


    override fun confirm() {
        val newExpense = NewExpense(
            title = title.value,
            price = price.value.toFloat(),
            user = users[userIndex.value],
            groupName = currentUserGroup.name,
            categoryName = categories[categoryIndex.value].name,
            expenseDate = date.value,
            methodOfPayment = methodsOfPayment[methodOfPaymentIndex.value].name,
            currencyCode = currencies[currencyIndex.value].symbol
        )

        coroutineScope.launch {
            expenseRepository.addExpense(newExpense)
            expenseRepository.getRecentExpense(groupRepository.currentUserGroup.value?.name!!)
            onBack()
        }
    }
}