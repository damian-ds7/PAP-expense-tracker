package pw.edu.pl.pap.screenComponents.groupScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.data.uiSetup.inputFields.InvitationData
import pw.edu.pl.pap.screenComponents.BaseScreenComponent
import pw.edu.pl.pap.ui.groupScreens.InvitationsScreen

open class InvitationsScreenComponent(
    baseComponent: BaseScreenComponent,
    val onDismiss: () -> Unit,
    private val group: UserGroup
) : BaseScreenComponent by baseComponent {

    var isNewInvitationsScreen: MutableState<Boolean> = mutableStateOf(false)
    var isPostSearchClicked: MutableState<Boolean> = mutableStateOf(false)

    //TODO
    // check if user is admin to block new invites if he is not

    private val _newInvitationInputFieldsData = mutableStateListOf<InputFieldData>()
    val newInvitationInputFieldsData: List<InputFieldData> get() = _newInvitationInputFieldsData

    private val _availableNewInvitationsData  = mutableStateListOf<InvitationData>()
    val availableNewInvitationsData: List<InvitationData> get() = _availableNewInvitationsData

    private val _receivedInvitationData = mutableStateListOf<InvitationData>()
    val receivedInvitationData: List<InvitationData> get() = _receivedInvitationData

    private val _sentInvitationData = mutableStateListOf<InvitationData>()
    val sentInvitationData: List<InvitationData> get() = _sentInvitationData

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
        runBlocking {
            //TODO
        }
    }

    fun search(){
        runBlocking{
            val users = apiService.userApiCLient.search(group.name, name.value, surname.value)
            _availableNewInvitationsData.clear()
            _availableNewInvitationsData.addAll(
                users.map { user ->
                    InvitationData.NewInvitationData(
                        receiver = user,
                        group = group,
                        onConfirm = { coroutineScope.launch { invite(user) } }
                    )
                }
            )
        }
        isPostSearchClicked.value = true
    }

    private fun invite(user: User){
        // (User, group)
        //TODO
    }

    fun cancelInvite(){
        //  (User, group / id [?])
        //TODO
    }

    fun acceptInvite(){
        // (User, group / id [?])
        //TODO
    }

    fun declineInvite(){
        // (User, group / id [?])
        //TODO
    }
}