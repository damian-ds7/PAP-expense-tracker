package pw.edu.pl.pap.navigation.loginSystem

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.KeyboardType
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import pw.edu.pl.pap.api.ApiService
import pw.edu.pl.pap.api.authApi.UserAuthApi
import pw.edu.pl.pap.data.inputFields.*
import pw.edu.pl.pap.util.sanitizePriceInput
import pw.edu.pl.pap.util.updatePrice

open class BaseLoginScreenComponent(
    componentContext: ComponentContext,
    protected val coroutineScope: CoroutineScope,
    protected val apiClient: UserAuthApi,
    val onConfirm: () -> Unit,
    val setToken: (String) -> Unit
) : ComponentContext by componentContext {

    protected open val _inputFieldsData = mutableStateListOf<InputFieldData>()
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData

    protected var email: MutableState<String> = mutableStateOf("")

    protected var password: MutableState<String> = mutableStateOf("")

    open fun setupInputFields() {
        _inputFieldsData.clear()
        _inputFieldsData.addAll(
            listOf(
                InputFieldData(
                    title = "Email: ",
                    textFieldData = TextFieldData(
                        parameter = email,
                        onChange = {
                            coroutineScope.launch { email.value = it }
                        }
                    )
                ),
                InputFieldData(
                    title = "Password",
                    isPassword = true,
                    textFieldData = TextFieldData(
                        parameter = password,
                        onChange = {
                            coroutineScope.launch {password.value = it}
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