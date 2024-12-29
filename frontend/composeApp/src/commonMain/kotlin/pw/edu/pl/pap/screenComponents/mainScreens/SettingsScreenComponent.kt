package pw.edu.pl.pap.screenComponents.mainScreens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import pw.edu.pl.pap.data.uiSetup.inputFields.ButtonData
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.data.uiSetup.inputFields.TextFieldData

class SettingsScreenComponent(
    baseComponent: BaseScreenComponent
) : BaseScreenComponent by baseComponent {

    private val _inputFieldsData = mutableStateListOf<InputFieldData>()
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData

    private var serverAddress: MutableState<String> = mutableStateOf(apiService.getCurrentUrl())

//    @Composable
//    suspend fun updateUrl() {
//
//    }


    fun setupInputFields() {
        _inputFieldsData.clear()
        _inputFieldsData.addAll(
            listOf(
                InputFieldData(
                    title = "Server address: ",
                    textFieldData = TextFieldData(
                        parameter = serverAddress,
                        onChange = {
                            coroutineScope.launch { serverAddress.value = it }
                        }
                    )
                ),
                InputFieldData(
                    title = "",
                    isButton = true,
                    buttonData = ButtonData(
                        title = "Edit personal user data",
                        onClick = {},
                    )
                ),
                InputFieldData(
                    title = "",
                    isButton = true,
                    buttonData = ButtonData(
                        title = "Change password",
                        onClick = {},
                    )
                ),
                InputFieldData(
                    title = "",
                    isButton = true,
                    buttonData = ButtonData(
                        title = "Edit preferences",
                        onClick = {},
                    )
                ),
                InputFieldData(
                    title = "",
                    isButton = true,
                    buttonData = ButtonData(
                        title = "LOG OUT",
                        onClick = {},
                    )
                ),
            )
        )
    }
}



//TODO set server address
//TODO log out
//TODO edit personal user data
//TODO change password
//TODO edit preferences
