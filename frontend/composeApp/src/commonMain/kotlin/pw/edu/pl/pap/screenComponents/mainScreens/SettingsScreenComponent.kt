package pw.edu.pl.pap.screenComponents.mainScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import pw.edu.pl.pap.data.uiSetup.inputFields.ButtonData
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.data.uiSetup.inputFields.TextFieldData
import androidx.compose.runtime.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import pw.edu.pl.pap.data.uiSetup.ConfirmationDialogConfig

class SettingsScreenComponent(
    private val onLogOut: () -> Unit,
    private val onUserPersonalsClicked: () -> Unit,
    private val onChangePasswordClicked: () -> Unit,
    private val onEditPreferencesClicked: () -> Unit,
    baseComponent: BaseScreenComponent
) : BaseScreenComponent by baseComponent {

    private val _inputFieldsData = mutableStateListOf<InputFieldData>()
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData

    var showLogOutDialog: MutableState<Boolean> = mutableStateOf(false)
    val logOutConfirmationData = ConfirmationDialogConfig(
        mainText = "Log Out",
        subText = "Are you sure you want to log out?",
        onNo = { showLogOutDialog.value = false },
        onYes = {
            showLogOutDialog.value = false
            onLogOut()
        }
    )

    private var serverAddress: MutableState<String> = mutableStateOf(apiService.getCurrentUrl())
    private var debounceJob by mutableStateOf<Job?>(null)

    private fun onServerAddressChange(newAddress: String) {
        serverAddress.value = newAddress
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(500)
//            println(apiService.getCurrentUrl())
//            println(serverAddress.value)
            updateUrl()
//            println("URL CHANGED")
        }
    }

    private fun updateUrl() {
        apiService.updateBaseUrl(serverAddress.value)
    }


    fun setupInputFields() {
        _inputFieldsData.clear()
        _inputFieldsData.addAll(
            listOf(
                InputFieldData(
                    title = "Server address: ",
                    textFieldData = TextFieldData(
                        parameter = serverAddress,
                        onChange = {
                            onServerAddressChange(it)
                        }
                    )
                ),
                InputFieldData(
                    title = "",
                    isButton = true,
                    buttonData = ButtonData(
                        title = "Edit personal user data",
                        onClick = {
                            coroutineScope.launch { onUserPersonalsClicked() }
                        },
                    )
                ),
                InputFieldData(
                    title = "",
                    isButton = true,
                    buttonData = ButtonData(
                        title = "Change password",
                        onClick = {
                            coroutineScope.launch { onChangePasswordClicked() }
                        },
                    )
                ),
                InputFieldData(
                    title = "",
                    isButton = true,
                    buttonData = ButtonData(
                        title = "Edit preferences",
                        onClick = {
                            coroutineScope.launch { onEditPreferencesClicked() }
                        },
                    )
                ),
                InputFieldData(
                    title = "",
                    isButton = true,
                    buttonData = ButtonData(
                        title = "LOG OUT",
                        isColored = true,
                        onClick = { showLogOutDialog.value = true }
                    )
                ),
            )
        )
    }
}



//TODO edit personal user data
//TODO change password
//TODO edit preferences
