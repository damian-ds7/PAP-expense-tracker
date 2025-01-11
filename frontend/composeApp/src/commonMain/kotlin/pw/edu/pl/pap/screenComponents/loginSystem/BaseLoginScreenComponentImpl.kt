package pw.edu.pl.pap.screenComponents.loginSystem

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.repositories.auth.LoginRepository

open class BaseLoginScreenComponentImpl(
    baseLoginScreenComponent: BaseLoginScreenComponent
) : BaseLoginScreenComponent by baseLoginScreenComponent {

    protected val loginRepository: LoginRepository by inject()

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
                InputFieldData.TextFieldData(
                    title = "Email: ",
                    parameter = email,
                    onChange = {
                        coroutineScope.launch { email.value = it }
                    },
                    textAlign = TextAlign.Left
                ),
                InputFieldData.TextFieldData(
                    title = "Password",
                    parameter = password,
                    onChange = {
                        coroutineScope.launch { password.value = it }
                    },
                    password = true,
                    textAlign = TextAlign.Left
                )
            )
        )
    }

    override fun confirm() {
        throw NotImplementedError("Subclasses must override confirm")
    }
}