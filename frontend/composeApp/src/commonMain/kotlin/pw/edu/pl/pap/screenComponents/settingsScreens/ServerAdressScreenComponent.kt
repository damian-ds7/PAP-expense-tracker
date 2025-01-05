package pw.edu.pl.pap.screenComponents.settingsScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import pw.edu.pl.pap.data.uiSetup.ConfirmationDialogConfig
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData


class ServerAdressScreenComponent(
    baseSettingsScreenComponent: BaseSettingsScreenComponent
) : BaseSettingsScreenComponentImpl(baseSettingsScreenComponent) {


    private var serverAddress: MutableState<String> = mutableStateOf(apiService.getCurrentUrl())

    override var confirmationData = ConfirmationDialogConfig(
        mainText = "Change Server Address",
        subText = "Are you sure you want to change server address?",
        onNo = { showConfirmationDialog.value = false },
        onYes = {
            showConfirmationDialog.value = false
            coroutineScope.launch { postChanges() }
            onBack()
        }
    )

    override fun postChanges() {
        apiService.updateBaseUrl(serverAddress.value)
    }

    override fun setupInputFields() {
        _inputFieldsData.clear()
        _inputFieldsData.addAll(
            listOf(
                InputFieldData.TextFieldData(
                    title = "Server address: ",
                    parameter = serverAddress,
                    onChange = {
                        coroutineScope.launch { serverAddress.value = it }
                    }
                )
            )
        )
    }
}