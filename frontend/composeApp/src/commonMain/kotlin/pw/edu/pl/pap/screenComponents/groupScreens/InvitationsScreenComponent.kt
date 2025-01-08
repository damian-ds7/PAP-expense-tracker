package pw.edu.pl.pap.screenComponents.groupScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.screenComponents.BaseScreenComponent

open class InvitationsScreenComponent(
    baseComponent: BaseScreenComponent,
    val onDismiss: () -> Unit,
    private val group: UserGroup
) : BaseScreenComponent by baseComponent {

    var isNewInvitationsScreen: MutableState<Boolean> = mutableStateOf(false)
    var isPostSearchClicked: MutableState<Boolean> = mutableStateOf(false)

    private val _newInvitationInputFieldsData = mutableStateListOf<InputFieldData>()
    val newInvitationInputFieldsData: List<InputFieldData> get() = _newInvitationInputFieldsData

    private val _availableNewInvitationsData  = mutableStateListOf<InputFieldData>()
    val availableNewInvitationsData: List<InputFieldData> get() = _availableNewInvitationsData

    private val _receivedInvitationData = mutableStateListOf<InputFieldData>()
    val receivedInvitationData: List<InputFieldData> get() = _receivedInvitationData

    private val _sentInvitationData = mutableStateListOf<InputFieldData>()
    val sentInvitationData: List<InputFieldData> get() = _sentInvitationData

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
                    title = "Surname: ",
                    parameter = surname,
                    onChange = {
                        coroutineScope.launch { surname.value = it }
                    }
                ),
            )
        )
    }

    fun fetchCurrentInvites(){
        //TODO
    }

    fun search(){
        //TODO
    }

    fun invite(){
        //TODO
    }

    fun acceptInvite(){
        //TODO
    }

    fun declineInvite(){
        //TODO
    }

    fun cancelInvite(){
        //TODO
    }

}