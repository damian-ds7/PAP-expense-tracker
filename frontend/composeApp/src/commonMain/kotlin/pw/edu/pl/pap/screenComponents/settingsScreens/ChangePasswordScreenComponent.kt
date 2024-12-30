package pw.edu.pl.pap.screenComponents.settingsScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pw.edu.pl.pap.data.uiSetup.ConfirmationDialogConfig
import pw.edu.pl.pap.data.uiSetup.inputFields.ButtonData
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.data.uiSetup.inputFields.TextFieldData
import pw.edu.pl.pap.screenComponents.mainScreens.BaseScreenComponent

class ChangePasswordScreenComponent (
    val onBack: () -> Unit,
    baseComponent: BaseScreenComponent
) : BaseScreenComponent by baseComponent {

    private val _inputFieldsData = mutableStateListOf<InputFieldData>()
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData

    private var password: MutableState<String> = mutableStateOf("")
    private var repeatedPassword: MutableState<String> = mutableStateOf("")

    var showPasswordsWarning: MutableState<Boolean> = mutableStateOf(false)

    var showConfirmationDialog: MutableState<Boolean> = mutableStateOf(false)
    val confirmationData = ConfirmationDialogConfig(
        mainText = "Change Password",
        subText = "Are you sure you want to change your password?",
        onNo = { showConfirmationDialog.value = false },
        onYes = {
            showConfirmationDialog.value = false
            coroutineScope.launch { saveNewPassword() }
            onBack()
        }
    )

    fun onConfirmClicked() {
        if (password.value != repeatedPassword.value || password.value == "") {
            showPasswordsWarning.value = true
            return
        } else {
            showConfirmationDialog.value = true
        }

    }

    private fun saveNewPassword() {
        //TODO
    }

    fun setupInputFields() {
        _inputFieldsData.clear()
        _inputFieldsData.addAll(
            listOf(
                InputFieldData(
                    title = "New password: ",
                    isPassword = true,
                    textFieldData = TextFieldData(
                        parameter = password,
                        onChange = {
                            coroutineScope.launch { password.value = it }
                        }
                    )
                ),
                InputFieldData(
                    title = "Repeat new password: ",
                    isPassword = true,
                    textFieldData = TextFieldData(
                        parameter = repeatedPassword,
                        onChange = {
                            coroutineScope.launch { repeatedPassword.value = it }
                        },
                    )
                )
            )
        )
    }
}