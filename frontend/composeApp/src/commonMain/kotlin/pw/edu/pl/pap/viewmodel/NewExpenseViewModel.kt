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
import pw.edu.pl.pap.apiclient.NewExpenseApiClient
import pw.edu.pl.pap.data.InputFieldData
import pw.edu.pl.pap.data.Record
import pw.edu.pl.pap.data.User

class NewExpenseViewModel(private val apiClient: NewExpenseApiClient) : ViewModel() {
    private val _inputFieldsData = mutableStateListOf<InputFieldData>() // Backing mutable state
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData // Immutable public view
    var record: MutableState<Record> = mutableStateOf(
        Record(0, 0f, User(0, "", "", ""))
    )

    init {
        setupInputFields()
    }

    private fun setupInputFields() {
        _inputFieldsData.addAll(
            listOf(
                InputFieldData(
                    title = "Price: ",
                    record = record,
                    onChange = { newParameter ->
                        updatePrice(newParameter)
                    }
                )
//                ,
//                InputFieldData(
//                    title = "Description",
//                    record = "",
//                    onChange = { newParameter: String ->
//                        updateField("Description", newParameter)
//                    }
//                )
            )
        )
    }

    fun updatePrice(newPrice: String) {
        try {
            val updatedRecord = record.value.copy(price = newPrice.toFloat())
            record.value = updatedRecord
            println("Updated record " + record.value.price)
        } catch (e: Exception) {
            println("Incorrect")
        }
    }
}

