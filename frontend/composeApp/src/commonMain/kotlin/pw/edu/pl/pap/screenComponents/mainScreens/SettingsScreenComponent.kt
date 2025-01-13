package pw.edu.pl.pap.screenComponents.mainScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.inject
import pw.edu.pl.pap.data.uiSetup.ConfirmationDialogConfig
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.repositories.data.UserRepository
import pw.edu.pl.pap.screenComponents.BaseComponent

class SettingsScreenComponent(
    private val onLogOut: () -> Unit,
    private val onChangeServerAddressClicked: () -> Unit,
    private val onUserPersonalsClicked: () -> Unit,
    private val onChangePasswordClicked: () -> Unit,
    private val onEditPreferencesClicked: () -> Unit,
    baseComponent: BaseComponent
) : BaseComponent by baseComponent {

    private val userRepository: UserRepository by inject()

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
        })

    var showDeleteAccountDialog: MutableState<Boolean> = mutableStateOf(false)
    val deleteAccountConfirmationData = ConfirmationDialogConfig(
        mainText = "Delete account",
        subText = "Are you sure you want to delete your account?\n" +
                  "     You will not be able to recover it",
        onNo = { showDeleteAccountDialog.value = false },
        onYes = {
            showDeleteAccountDialog.value = false
            onLogOut()
            coroutineScope.launch { deleteAccount() }
        })
//    private var debounceJob by mutableStateOf<Job?>(null)
//
//    private fun onServerAddressChange(newAddress: String) {
//        serverAddress.value = newAddress
//        debounceJob?.cancel()
//        debounceJob = coroutineScope.launch {
//            delay(500)
////            println(apiService.getCurrentUrl())
////            println(serverAddress.value)
//            updateUrl()
////            println("URL CHANGED")
//        }
//    }

    fun setupInputFields() {
        _inputFieldsData.clear()
        _inputFieldsData.addAll(
            listOf(
            InputFieldData.ButtonData(
                title = "Edit personal user data",
                onClick = {
                    coroutineScope.launch { onUserPersonalsClicked() }
                },
            ),
            InputFieldData.ButtonData(
                title = "Change password",
                onClick = {
                    coroutineScope.launch { onChangePasswordClicked() }
                },
            ),
            InputFieldData.ButtonData(
                title = "Edit preferences",
                onClick = {
                    coroutineScope.launch { onEditPreferencesClicked() }
                },
            ),
            InputFieldData.ButtonData(
                title = "LOG OUT", isColored = true, onClick = { showLogOutDialog.value = true }
            ),
            InputFieldData.ButtonData(
                title = "DELETE ACCOUNT", isColored = true, onClick = { showDeleteAccountDialog.value = true }
            )
        ))
    }

    private suspend fun deleteAccount() {
        userRepository.deleteUser()
        //TODO test it
    }
}





