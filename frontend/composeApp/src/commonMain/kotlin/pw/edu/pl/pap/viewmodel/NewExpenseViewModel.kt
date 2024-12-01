package pw.edu.pl.pap.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.utils.io.core.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import pw.edu.pl.pap.apiclient.ApiClient
import pw.edu.pl.pap.data.InputFieldData
import pw.edu.pl.pap.data.Record
import pw.edu.pl.pap.data.User
import pw.edu.pl.pap.data.Category

class NewExpenseViewModel(private val apiClient: ApiClient) : ViewModel() {
    private val _inputFieldsData = mutableStateListOf<InputFieldData>() // Backing mutable state
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData // Immutable public view

    var price: MutableState<String> = mutableStateOf("")

    init {
        setupInputFields()
    }

    private fun setupInputFields() {
        _inputFieldsData.addAll(
            listOf(
                InputFieldData(
                    title = "Price: ",
                    parameter = price,
                    onChange = { newParameter ->
                        updatePrice(newParameter)
                    }
                )
//                ,
//                InputFieldData(
//                    title = "Description",
//                    record = record,
//                    onChange = { newParameter: String ->
//                        updateField("Description", newParameter)
//                    }
//                )
            )
        )
    }

    private fun updatePrice(newPrice: String) {
        try {
            price.value = newPrice
            println("Updated record " + price.value)
        } catch (e: Exception) {
            println("Incorrect")
        }
    }



    fun expenseConfirmed() {
        // download to set ID
        // find user
        // save record
        val date: LocalDate = Clock.System.todayIn(TimeZone.UTC)
        val record = Record(0, price.value.toFloat(), date, User(0, "", "", ""), Category(0, "") )
        println("confirmed " + record.price)
    }
}

