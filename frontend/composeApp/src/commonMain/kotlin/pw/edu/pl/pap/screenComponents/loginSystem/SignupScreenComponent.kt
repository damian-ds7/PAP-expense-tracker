package pw.edu.pl.pap.screenComponents.loginSystem

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.data.uiSetup.inputFields.TextFieldData
import pw.edu.pl.pap.util.validateEmail

class SignupScreenComponent(
    baseScreenComponent: BaseLoginScreenComponent
) : BaseLoginScreenComponentImpl(baseScreenComponent) {

    private var confirmedPassword: MutableState<String> = mutableStateOf("")

    private var name: MutableState<String> = mutableStateOf("")

    private var surname: MutableState<String> = mutableStateOf("")

    var showPasswordsWarning: MutableState<Boolean> = mutableStateOf(false)

    override fun setupInputFields() {
        super.setupInputFields()
        _inputFieldsData.addAll(
            listOf(
                InputFieldData(
                    title = "Confirm password",
                    isPassword = true,
                    textFieldData = TextFieldData(
                        parameter = confirmedPassword,
                        onChange = {
                            coroutineScope.launch { confirmedPassword.value = it }
                        },
                    )
                ),
                InputFieldData(
                    title = "Name",
                    textFieldData = TextFieldData(
                        parameter = name,
                        onChange = {
                            coroutineScope.launch { name.value = it }
                        },
                    )
                ),
                InputFieldData(
                    title = "Surname",
                    textFieldData = TextFieldData(
                        parameter = surname,
                        onChange = {
                            coroutineScope.launch { surname.value = it }
                        },
                    )
                )
            )
        )
    }

    override fun confirm() {
        if (!validateEmail(email.value)) {
            showEmailWarning.value = true
            return
        }
        if (password.value != confirmedPassword.value || password.value == "") {
            showPasswordsWarning.value = true
            return
        }
        //TODO push new user
        //TODO wait for response
        //TODO set token
            showFailedLoginWarning.value = true
            //TODO set failedLoginMessage
            failedLoginMessage.value = "Something went wrong"
        onConfirm()
    }

}