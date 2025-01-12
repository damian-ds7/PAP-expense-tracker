package pw.edu.pl.pap.screenComponents.singleExpense

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.koin.core.component.inject
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.repositories.data.ConfigRepository
import pw.edu.pl.pap.repositories.data.ExpenseRepository
import pw.edu.pl.pap.repositories.data.GroupRepository
import pw.edu.pl.pap.screenComponents.BaseComponent
import pw.edu.pl.pap.util.sanitizePriceInput
import pw.edu.pl.pap.util.updatePrice

open class BaseExpenseScreenComponent(
    baseComponent: BaseComponent,
    val onBack: () -> Unit,
) : BaseComponent by baseComponent {

    protected val expenseRepository: ExpenseRepository by inject()
    private val configRepository: ConfigRepository by inject()
    protected val groupRepository: GroupRepository by inject()

    private val _inputFieldsData = mutableStateListOf<InputFieldData>()
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData

    protected open var title: MutableState<String> = mutableStateOf("")

    protected val categories = configRepository.categories.value
    protected open var categoryIndex: MutableState<Int> = mutableStateOf(0)

    protected open var date: MutableState<LocalDate> = mutableStateOf(Clock.System.todayIn(TimeZone.UTC))

    protected open var price: MutableState<String> = mutableStateOf("")

    protected val currencies = configRepository.currencies.value
    protected open var currencyIndex: MutableState<Int> = mutableStateOf(0)

    protected val methodsOfPayment = configRepository.paymentMethods.value
    protected open var methodOfPaymentIndex: MutableState<Int> = mutableStateOf(0)

    protected var users = groupRepository.usersInCurrentGroup.value
    private var userNames = users.map { "${it.name} ${it.surname}" }
    protected open var userIndex: MutableState<Int> = mutableStateOf(0)

    val canConfirm by derivedStateOf { price.value.isNotEmpty() }

    fun setupInputFields() {
        _inputFieldsData.clear()
        _inputFieldsData.addAll(
            listOf(
                InputFieldData.TextFieldData(
                    title = "Title: ",
                    parameter = title,
                    onChange = {
                        coroutineScope.launch { title.value = it }
                    }
                ),
                InputFieldData.DropdownListData(
                    title = "Category: ",
                    itemList = categories.map { it.name },
                    selectedIndex = categoryIndex,
                    onItemClick = {
                        coroutineScope.launch { categoryIndex.value = it }
                    }
                ),
                InputFieldData.DatePickerData(
                    title = "Date: ",
                    date = date,
                    onDateConfirm = { newDate ->
                        date.value = newDate
                    }
                ),
                InputFieldData.TextFieldData(
                    title = "Price: ",
                    parameter = price,
                    onChange = { newParameter ->
                        val sanitizedInput = sanitizePriceInput(newParameter)

                        if (sanitizedInput != null) {
                            coroutineScope.launch { updatePrice(sanitizedInput, price) }
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                ),
                InputFieldData.DropdownListData(
                    title = "Currency",
                    itemList = currencies.map { it.symbol },
                    selectedIndex = currencyIndex,
                    onItemClick = {
                        currencyIndex.value = it
                    }
                ),
                InputFieldData.DropdownListData(
                    title = "Method of payment: ",
                    itemList = methodsOfPayment.map { it.name },
                    selectedIndex = methodOfPaymentIndex,
                    onItemClick = {
                        coroutineScope.launch { methodOfPaymentIndex.value = it }
                    }
                ),
                InputFieldData.DropdownListData(
                    title = "User: ",
                    itemList = userNames,
                    selectedIndex = userIndex,
                    onItemClick = {
                        coroutineScope.launch { userIndex.value = it }
                    }

                )
            )
        )
    }

    open fun confirm() {
        throw NotImplementedError("Subclasses must override confirm")
    }
}