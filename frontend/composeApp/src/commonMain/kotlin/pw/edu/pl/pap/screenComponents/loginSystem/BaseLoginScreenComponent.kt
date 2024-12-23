package pw.edu.pl.pap.screenComponents.loginSystem

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pw.edu.pl.pap.api.authApi.UserAuthApi
import pw.edu.pl.pap.data.uiSetup.inputFields.*

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