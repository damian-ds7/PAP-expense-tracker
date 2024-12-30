package pw.edu.pl.pap.screenComponents.settingsScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import pw.edu.pl.pap.data.uiSetup.ConfirmationDialogConfig
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.data.uiSetup.inputFields.TextFieldData
import pw.edu.pl.pap.screenComponents.mainScreens.BaseScreenComponent

class ChangePasswordScreenComponent (
    baseSettingsScreenComponent: BaseSettingsScreenComponent
) : BaseSettingsScreenComponentImpl(baseSettingsScreenComponent) {

    private var password: MutableState<String> = mutableStateOf("")
    private var repeatedPassword: MutableState<String> = mutableStateOf("")

    var showPasswordsWarning: MutableState<Boolean> = mutableStateOf(false)

    override var confirmationData = ConfirmationDialogConfig(
        mainText = "Change Password",
        subText = "Are you sure you want to change your password?",
        onNo = { showConfirmationDialog.value = false },
        onYes = {
            showConfirmationDialog.value = false
            coroutineScope.launch { postChanges() }
            onBack()
        }
    )

    override fun onConfirmClicked() {
        if (password.value != repeatedPassword.value || password.value == "") {
            showPasswordsWarning.value = true
            return
        } else {
            showConfirmationDialog.value = true
        }

    }

    override fun postChanges() {
        //TODO
    }

    override fun setupInputFields() {
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