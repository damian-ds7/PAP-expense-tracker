package pw.edu.pl.pap.screenComponents.settingsScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import pw.edu.pl.pap.data.uiSetup.ConfirmationDialogConfig
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData

class ChangePasswordScreenComponent(
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
            super.onConfirmClicked()
        }

    }

    override fun postChanges() {
        //TODO
    }

    override fun setupInputFields() {
        _inputFieldsData.clear()
        _inputFieldsData.addAll(
            listOf(
                InputFieldData.TextFieldData(
                    title = "New password: ",
                    parameter = password,
                    onChange = {
                        coroutineScope.launch { password.value = it }
                    },
                    password = true
                ),
                InputFieldData.TextFieldData(
                    title = "Repeat new password: ",
                    parameter = repeatedPassword,
                    onChange = {
                        coroutineScope.launch { repeatedPassword.value = it }
                    },
                    password = true
                )
            )
        )
    }
}