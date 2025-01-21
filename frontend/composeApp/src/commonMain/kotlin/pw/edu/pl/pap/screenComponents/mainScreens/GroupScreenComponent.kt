package pw.edu.pl.pap.screenComponents.mainScreens

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.inject
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.repositories.data.GroupRepository
import pw.edu.pl.pap.repositories.data.UserRepository
import pw.edu.pl.pap.screenComponents.BaseComponent

class GroupScreenComponent(
    private val onUserClicked: (UserGroup, User) -> Unit,
    val onEditGroupClicked: () -> Unit,
    val onNewGroupClicked: () -> Unit,
    val onInvitationsClicked: () -> Unit,
    baseComponent: BaseComponent,
) : BaseComponent by baseComponent {

    private val userRepository: UserRepository by inject()
    private val groupRepository: GroupRepository by inject()
    var currentUserGroup = groupRepository.currentUserGroup

    private val _inputFieldsData = mutableStateListOf<InputFieldData>()
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData

    var isAdmin = userRepository.isAdmin

    private var users = groupRepository.usersInCurrentGroup.value
    private var userNames = users.map { "${it.name} ${it.surname}" }

    init {
        coroutineScope.launch {
            updateUsers()
        }
    }

    fun updateUsers() {
        runBlocking{ getUsers() }
        users = groupRepository.usersInCurrentGroup.value
        userNames = users.map { "${it.name} ${it.surname}" }
        if (currentUserGroup.value != null) {
            runBlocking {userRepository.checkIsAdmin(currentUserGroup.value!!)}
            isAdmin = userRepository.isAdmin
        }
        setupInputFields()
    }

    private suspend fun getUsers() {
        groupRepository.getUsersInCurrentGroup()
    }


    fun setupInputFields() {
        _inputFieldsData.clear()
        _inputFieldsData.addAll(
            userNames.mapIndexed { index, username ->
                val user = users[index]
                InputFieldData.UserButtonData(
                    title = username,
                    onClick = { onUserClicked(currentUserGroup.value!!, user) }
                )
            }
        )
    }

    fun refreshGroup() {
        runBlocking { groupRepository.refreshGroups() }
        users = groupRepository.usersInCurrentGroup.value
        userNames = users.map { "${it.name} ${it.surname}" }
        currentUserGroup = groupRepository.currentUserGroup
        if (currentUserGroup.value != null){
            setupInputFields()
        }
    }
}