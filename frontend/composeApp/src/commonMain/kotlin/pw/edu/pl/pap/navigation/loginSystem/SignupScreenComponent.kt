package pw.edu.pl.pap.navigation.loginSystem

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pw.edu.pl.pap.api.authApi.UserAuthApi
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.data.uiSetup.inputFields.TextFieldData

class SignupScreenComponent(
    componentContext: ComponentContext,
    coroutineScope: CoroutineScope,
    apiClient: UserAuthApi,
    onConfirm: () -> Unit,
    setToken: (String) -> Unit
) : BaseLoginScreenComponent(componentContext, coroutineScope, apiClient, onConfirm, setToken){

    private var confirmedPassword: MutableState<String> = mutableStateOf("")

    private var name: MutableState<String> = mutableStateOf("")

    private var surname: MutableState<String> = mutableStateOf("")

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
        //TODO verify email
        //TODO verify passwords
        //TODO show warning screen if incorrect data
        //TODO push new user
        //TODO wait for response
        //TODO set token
        onConfirm()
    }

}