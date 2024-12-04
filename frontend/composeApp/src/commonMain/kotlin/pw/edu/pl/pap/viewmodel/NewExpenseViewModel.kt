package pw.edu.pl.pap.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.utils.io.core.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import pw.edu.pl.pap.apiclient.ApiClient
import pw.edu.pl.pap.data.InputFieldData
import pw.edu.pl.pap.data.NewExpense
import pw.edu.pl.pap.data.User
import pw.edu.pl.pap.data.Category
import pw.edu.pl.pap.util.updatePrice


class NewExpenseViewModel(private val apiClient: ApiClient) : ViewModel() {
    private val _inputFieldsData = mutableStateListOf<InputFieldData>() // Backing mutable state
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData // Immutable public view


    var price: MutableState<String> = mutableStateOf("")


    @Composable
    fun setupInputFields() {
        _inputFieldsData.addAll(
            listOf(
                InputFieldData(
                    title = "Price: ",
                    parameter = price,
                    onChange = { newParameter ->
                        viewModelScope.launch { updatePrice(newParameter, price) }
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




    @Composable
    fun expenseConfirmed(onConfirm: () -> Unit) {
        // download to set ID
        // find user
        // save record
//        val id: Long = 5
        val user: User = User(1, "Marcinek", "Marcinkowski", "Kaczka2137@gmail.com")
        val date: LocalDate = Clock.System.todayIn(TimeZone.UTC)
//        val category: Category = Category(5, "Test")
//        val record: Record = Record(id, price.value.toFloat(), user, date, category )
        val newExpense: NewExpense = NewExpense(price.value.toFloat(), date, user)

        viewModelScope.launch{
            apiClient.postNewExpense(newExpense)
            onConfirm()
        }

        println("confirmed " + newExpense.price)
    }
}




