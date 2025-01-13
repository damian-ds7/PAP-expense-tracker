package pw.edu.pl.pap.screenComponents.singleExpense

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import pw.edu.pl.pap.data.databaseAssociatedData.Currency
import pw.edu.pl.pap.data.databaseAssociatedData.NewExpense
import pw.edu.pl.pap.data.databaseAssociatedData.PaymentMethod
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.repositories.data.UserRepository
import pw.edu.pl.pap.screenComponents.BaseComponent
import pw.edu.pl.pap.util.listFunctions.getIndexByField

class NewExpenseScreenComponent(
    baseComponent: BaseComponent,
    onBack: () -> Unit,
    private val currentUserGroup: UserGroup,
) : BaseExpenseScreenComponent(baseComponent, onBack) {

    private val userRepository: UserRepository by inject()
    private val preferences = userRepository.currentPreferences.value

    override var currencyIndex: MutableState<Int> =
        mutableStateOf(getIndexByField(currencies, preferences?.currencySymbol, Currency::symbol) ?: 0)

    override var methodOfPaymentIndex: MutableState<Int> =
        mutableStateOf(getIndexByField(methodsOfPayment, preferences?.methodOfPayment, PaymentMethod::name) ?: 0)

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