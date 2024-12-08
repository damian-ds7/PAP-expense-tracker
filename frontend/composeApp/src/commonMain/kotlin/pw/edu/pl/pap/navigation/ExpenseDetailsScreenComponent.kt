package pw.edu.pl.pap.navigation

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import com.arkivanov.decompose.ComponentContext
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pw.edu.pl.pap.apiclient.ApiClient
import pw.edu.pl.pap.data.Expense
import pw.edu.pl.pap.data.InputFieldData
import pw.edu.pl.pap.util.sanitizePriceInput
import pw.edu.pl.pap.util.updatePrice

class ExpenseDetailsScreenComponent(
    componentContext: ComponentContext,
    private val apiClient: ApiClient,
    private val coroutineScope: CoroutineScope,
    val expense: Expense,
    val onBack: () -> Unit
) : ComponentContext by componentContext {

    private val _inputFieldsData = mutableStateListOf<InputFieldData>()
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData

    private var newPrice: MutableState<String> = mutableStateOf("${expense.price}")
    val canSave by derivedStateOf { newPrice.value.isNotEmpty() }

    fun setupInputFields() {
        _inputFieldsData.clear()
        _inputFieldsData.addAll(
            listOf(
                InputFieldData(
                    title = "Price: ",
                    parameter = newPrice,
                    onChange = { newParameter ->
                        val sanitizedInput = sanitizePriceInput(newParameter)

                        if (sanitizedInput != null) {
                            coroutineScope.launch { updatePrice(sanitizedInput, newPrice) }
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            )
        )
    }

    fun confirmChanges() {
        val newExpense = expense.copy(price = newPrice.value.toFloat())

        if (newExpense == expense) {
            onBack()
            return
        }

        coroutineScope.launch {
            if (apiClient.updateExpense(newExpense).status.isSuccess()) {
                onBack()
            }
        }

        println("Updated Expense ${newExpense.id} from ${expense.price} to ${newExpense.price}")
    }
}