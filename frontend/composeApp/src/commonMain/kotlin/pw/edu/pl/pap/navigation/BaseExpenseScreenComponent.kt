package pw.edu.pl.pap.navigation

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pw.edu.pl.pap.apiclient.ApiClient
import pw.edu.pl.pap.data.inputFields.DropdownListData
import pw.edu.pl.pap.data.inputFields.InputFieldData
import pw.edu.pl.pap.data.inputFields.TextFieldData
import pw.edu.pl.pap.util.sanitizePriceInput
import pw.edu.pl.pap.util.updatePrice

open class BaseExpenseScreenComponent(
    componentContext: ComponentContext,
    protected val apiClient: ApiClient,
    protected val coroutineScope: CoroutineScope,
    val onBack: () -> Unit
) : ComponentContext by componentContext {

    private val _inputFieldsData = mutableStateListOf<InputFieldData>()
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData

    protected open var newPrice: MutableState<String> = mutableStateOf("")

    private val currencies = listOf("PLN", "EUR", "USD")
    protected  open var selectedIndex: MutableState<Int> = mutableStateOf(0)

    val canConfirm by derivedStateOf { newPrice.value.isNotEmpty() }

    fun setupInputFields() {
        _inputFieldsData.clear()
        _inputFieldsData.addAll(
            listOf(
                InputFieldData(
                    title = "Price: ",
                    isDropdownList = false,
                    textFieldData = TextFieldData(
                        parameter = newPrice,
                        onChange = { newParameter ->
                            val sanitizedInput = sanitizePriceInput(newParameter)

                            if (sanitizedInput != null) {
                                coroutineScope.launch { updatePrice(sanitizedInput, newPrice) }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                ),
                InputFieldData(
                    title = "Currency",
                    isDropdownList = true,
                    dropdownListData = DropdownListData(
                        itemList = currencies,
                        selectedIndex = selectedIndex,
                        onItemClick = {
                            selectedIndex.value = it
                        }
                    )
                )

            )
        )
    }

    open fun confirm() {
        throw NotImplementedError("Subclasses must override confirm")
    }
}