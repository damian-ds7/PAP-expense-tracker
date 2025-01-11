package pw.edu.pl.pap.screenComponents.singleExpense

import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import pw.edu.pl.pap.data.databaseAssociatedData.NewExpense
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.repositories.data.GroupRepository
import pw.edu.pl.pap.screenComponents.BaseComponent

class NewExpenseScreenComponent(
    baseComponent: BaseComponent,
    onBack: () -> Unit,
    private val currentUserGroup: UserGroup,
) : BaseExpenseScreenComponent(baseComponent, onBack) {

    private val groupRepository: GroupRepository by inject(GroupRepository::class.java)

    init {
        setupInputFields()
    }

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

        coroutineScope.launch {
            expenseRepository.addExpense(newExpense)
            expenseRepository.getRecentExpense(groupRepository.currentUserGroup.value?.name!!)
            onBack()
        }
    }
}