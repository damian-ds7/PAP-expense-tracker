package pw.edu.pl.pap.screenComponents.loginSystem

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import pw.edu.pl.pap.data.databaseAssociatedData.UserLoginData
import pw.edu.pl.pap.data.databaseAssociatedData.UserSignUpData
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.repositories.auth.LoginRepository
import pw.edu.pl.pap.repositories.auth.SignupRepository
import pw.edu.pl.pap.util.validateEmail

class SignupScreenComponent(
    baseScreenComponent: BaseLoginScreenComponent
) : BaseLoginScreenComponentImpl(baseScreenComponent) {

    private val signupRepository: SignupRepository by inject()

    private var confirmedPassword: MutableState<String> = mutableStateOf("")

    private var name: MutableState<String> = mutableStateOf("")

    private var surname: MutableState<String> = mutableStateOf("")

    var showPasswordsWarning: MutableState<Boolean> = mutableStateOf(false)

    override fun setupInputFields() {
        super.setupInputFields()
        _inputFieldsData.addAll(
            listOf(
                InputFieldData.TextFieldData(
                    title = "Confirm password",
                    parameter = confirmedPassword,
                    onChange = {
                        coroutineScope.launch { confirmedPassword.value = it }
                    },
                    password = true,
                    textAlign = TextAlign.Left
                ),
                InputFieldData.TextFieldData(
                    title = "Name",
                    parameter = name,
                    onChange = {
                        coroutineScope.launch { name.value = it }
                    },
                    textAlign = TextAlign.Left
                ),
                InputFieldData.TextFieldData(
                    title = "Surname",
                    parameter = surname,
                    onChange = {
                        coroutineScope.launch { surname.value = it }
                    },
                    textAlign = TextAlign.Left
                )
            )
        )
    }

    override fun confirm() {
//        if (!validateEmail(email.value)) {
//            showEmailWarning.value = true
//            return
//        }
//        if (password.value != confirmedPassword.value || password.value == "") {
//            showPasswordsWarning.value = true
//            return
//        }
//        val userSignUpData = UserSignUpData(name.value, surname.value, email.value, password.value)

        val userSignUpData = UserSignUpData("Maciek", "ZPasiek", "MaciekZPasiek2@gmail.com", "123")

        coroutineScope.launch {
            val response = signupRepository.signup(userSignUpData)
            if (response) {
                onConfirm()
            } else {
                showFailedLoginWarning.value = true
                failedLoginMessage.value = "Something went wrong"
            }
        }
    }

}