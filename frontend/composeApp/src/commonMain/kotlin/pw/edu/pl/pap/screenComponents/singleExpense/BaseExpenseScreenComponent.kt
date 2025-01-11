package pw.edu.pl.pap.screenComponents.singleExpense

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.koin.core.component.inject
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.repositories.data.ExpenseRepository
import pw.edu.pl.pap.screenComponents.BaseComponent
import pw.edu.pl.pap.util.sanitizePriceInput
import pw.edu.pl.pap.util.updatePrice

open class BaseExpenseScreenComponent(
    baseComponent: BaseComponent,
    val onBack: () -> Unit,
) : BaseComponent by baseComponent {

    protected val expenseRepository: ExpenseRepository by inject()

    private val _inputFieldsData = mutableStateListOf<InputFieldData>()
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData

    protected open var title: MutableState<String> = mutableStateOf("")

    protected val categories = listOf("food", "transport", "bills")

    //TODO fetch categories
    protected open var categoryIndex: MutableState<Int> = mutableStateOf(0)


    protected open var date: MutableState<LocalDate> = mutableStateOf(Clock.System.todayIn(TimeZone.UTC))


    protected open var price: MutableState<String> = mutableStateOf("")

    protected val currencies = listOf("PLN", "EUR", "USD")

    //TODO fetch currencies
    protected open var currencyIndex: MutableState<Int> = mutableStateOf(0)

    protected val methodsOfPayment = listOf("cash", "Card", "W naturze")

    //TODO fetch methods of payment
    protected open var methodOfPaymentIndex: MutableState<Int> = mutableStateOf(0)

    //temp
    protected val users = listOf(
        User(1, "Herkules", "1", "Kaczka2137@gmail.com"),
        User(2, "Zeus", "2", "Kaczka2137@gmail.com"),
        User(3, "Posejdon", "3", "Kaczka2137@gmail.com"),
    )
    private val userNames = users.map { "${it.name} ${it.surname}" }

    //TODO fetch available users
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
                    itemList = categories,
                    selectedIndex = categoryIndex,
                    onItemClick = {
                        coroutineScope.launch { categoryIndex.value = it }
                    }
                ),
                InputFieldData.DatePickerData(
                    title = "Date: ",
                    date = date,
                    onDateConfirm = { millis ->
                        date.value = LocalDate.fromEpochDays((millis / (24 * 60 * 60 * 1000)).toInt())
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
                    itemList = currencies,
                    selectedIndex = currencyIndex,
                    onItemClick = {
                        currencyIndex.value = it
                    }
                ),
                InputFieldData.DropdownListData(
                    title = "Method of payment: ",
                    itemList = methodsOfPayment,
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