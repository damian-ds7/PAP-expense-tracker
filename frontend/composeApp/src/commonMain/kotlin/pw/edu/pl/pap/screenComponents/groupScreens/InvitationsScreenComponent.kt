package pw.edu.pl.pap.screenComponents.groupScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.screenComponents.BaseScreenComponent

open class InvitationsScreenComponent(
    baseComponent: BaseScreenComponent,
    val onDismiss: () -> Unit
) : BaseScreenComponent by baseComponent {

    var isNewInvitationsScreen: MutableState<Boolean> = mutableStateOf(false)

    private val _newInvitationInputFieldsData = mutableStateListOf<InputFieldData>()
    val newInvitationInputFieldsData: List<InputFieldData> get() = _newInvitationInputFieldsData

    private val _pendingInvitationInputFieldsData = mutableStateListOf<InputFieldData>()
    val pendingInvitationInputFieldsData: List<InputFieldData> get() = _pendingInvitationInputFieldsData

    protected open var name: MutableState<String> = mutableStateOf("")
    protected open var surname: MutableState<String> = mutableStateOf("")

    fun setupNewInvitationInputFields() {
        _newInvitationInputFieldsData.clear()
        _newInvitationInputFieldsData.addAll(
            listOf(
                InputFieldData.TextFieldData(
                    title = "Name: ",
                    parameter = name,
                    onChange = {
                        coroutineScope.launch { name.value = it }
                    }
                ),
                InputFieldData.TextFieldData(
                    title = "Name: ",
                    parameter = surname,
                    onChange = {
                        coroutineScope.launch { surname.value = it }
                    }
                ),
            )
        )
    }



}