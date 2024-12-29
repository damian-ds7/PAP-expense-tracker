package pw.edu.pl.pap.screenComponents.loginSystem

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import pw.edu.pl.pap.data.uiSetup.inputFields.*

open class BaseLoginScreenComponentImpl(
    baseLoginScreenComponent: BaseLoginScreenComponent
) : BaseLoginScreenComponent by baseLoginScreenComponent {

    protected open val _inputFieldsData = mutableStateListOf<InputFieldData>()
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData

    protected var email: MutableState<String> = mutableStateOf("")

    protected var password: MutableState<String> = mutableStateOf("")

    var showEmailWarning: MutableState<Boolean> = mutableStateOf(false)

    var showFailedLoginWarning: MutableState<Boolean> = mutableStateOf(false)
    var failedLoginMessage: MutableState<String> = mutableStateOf("")

    override fun setupInputFields() {
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

    override fun confirm() {
        throw NotImplementedError("Subclasses must override confirm")
    }
}