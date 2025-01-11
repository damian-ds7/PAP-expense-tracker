package pw.edu.pl.pap.screenComponents.groupScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.inject
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.data.uiSetup.inputFields.InvitationData
import pw.edu.pl.pap.repositories.data.GroupRepository
import pw.edu.pl.pap.repositories.data.MembershipRepository
import pw.edu.pl.pap.repositories.data.TemporaryMembershipRepository
import pw.edu.pl.pap.repositories.data.UserRepository
import pw.edu.pl.pap.screenComponents.BaseComponent
import pw.edu.pl.pap.ui.groupScreens.InvitationsScreen

open class InvitationsScreenComponent(
    baseComponent: BaseComponent,
    val onDismiss: () -> Unit
) : BaseComponent by baseComponent {

    private val userRepository: UserRepository by inject()
    private val membershipRepository: MembershipRepository by inject()
    private val temporaryMembershipRepository: TemporaryMembershipRepository by inject()

    private val groupRepository: GroupRepository by inject()
    val currentUserGroup = groupRepository.currentUserGroup

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
            temporaryMembershipRepository.getPendingInvitations()
        }
    }

    private fun setupReceivedInvitationsData() {
        _receivedInvitationData.clear()
        _receivedInvitationData.addAll(
            temporaryMembershipRepository.invitationsReceived.value.map { invitation ->
                InvitationData.ReceivedInvitationData(
                    sender = invitation.sender,
                    id = invitation.id,
                    group = invitation.group,
                    onConfirm = { coroutineScope.launch { acceptInvite(invitation.id) }},
                    onCancel = { coroutineScope.launch { declineInvite(invitation.id)}}
                )
            }
        )
    }

    private fun setupSentInvitationsData() {
        _sentInvitationData.clear()
        _sentInvitationData.addAll(
            temporaryMembershipRepository.invitationsSent.value.map { invitation ->
                InvitationData.SentInvitationData(
                    receiver = invitation.receiver,
                    id = invitation.id,
                    group = invitation.group,
                    onCancel = { coroutineScope.launch { declineInvite(invitation.id)}}
                )
            }
        )
    }

    fun search(){
        runBlocking{
            userRepository.searchUsers(currentUserGroup.value!!, name.value, surname.value)
            _availableNewInvitationsData.clear()
            _availableNewInvitationsData.addAll(
                userRepository.usersFound.value.map { user ->
                    InvitationData.NewInvitationData(
                        receiver = user,
                        group = currentUserGroup.value!!,
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

    fun acceptInvite(id: Long){
        // (User, group / id [?])
        //TODO
    }

    fun declineInvite(id: Long){
        // (User, group / id [?])
        //TODO
    }
}