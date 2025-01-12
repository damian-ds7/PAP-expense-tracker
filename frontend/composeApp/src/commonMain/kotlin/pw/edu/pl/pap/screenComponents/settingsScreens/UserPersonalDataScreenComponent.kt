package pw.edu.pl.pap.screenComponents.settingsScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.inject
import pw.edu.pl.pap.data.uiSetup.ConfirmationDialogConfig
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.repositories.data.ConfigRepository

class UserPersonalDataScreenComponent(
    baseSettingsScreenComponent: BaseSettingsScreenComponent
) : BaseSettingsScreenComponentImpl(baseSettingsScreenComponent) {

    private val configRepository: ConfigRepository by inject()

    private var email: MutableState<String> = mutableStateOf(configRepository.currentUserInfo.value!!.email)
    private var name: MutableState<String> = mutableStateOf(configRepository.currentUserInfo.value!!.name)
    private var surname: MutableState<String> = mutableStateOf(configRepository.currentUserInfo.value!!.surname)

    override var confirmationData = ConfirmationDialogConfig(
        mainText = "Change Personal Data",
        subText = "Are you sure you want to change your personals?",
        onNo = { showConfirmationDialog.value = false },
        onYes = {
            showConfirmationDialog.value = false
            coroutineScope.launch { postChanges() }
            onBack()
        }
    )

    override fun postChanges() {
        //TODO
    }

    override fun setupInputFields() {
        _inputFieldsData.clear()
        _inputFieldsData.addAll(
            listOf(
                InputFieldData.TextFieldData(
                    title = "Email: ",
                    parameter = email,
                    onChange = {
                        coroutineScope.launch { email.value = it }
                    }
                ),
                InputFieldData.TextFieldData(
                    title = "Name: ",
                    parameter = name,
                    onChange = {
                        coroutineScope.launch { name.value = it }
                    },
                ),
                InputFieldData.TextFieldData(
                    title = "Surname: ",
                    parameter = surname,
                    onChange = {
                        coroutineScope.launch { surname.value = it }
                    },
                )
            )
        )
    }
}