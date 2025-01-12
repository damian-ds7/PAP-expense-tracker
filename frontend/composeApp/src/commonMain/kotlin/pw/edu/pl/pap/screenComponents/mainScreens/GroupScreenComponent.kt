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

    private val groupRepository: GroupRepository by inject()
    val currentUserGroup = groupRepository.currentUserGroup

    private val _inputFieldsData = mutableStateListOf<InputFieldData>()
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData

    private var users = groupRepository.usersInCurrentGroup.value
    private var userNames = users.map { "${it.name} ${it.surname}" }

    private val currentGroup = groupRepository.currentUserGroup

    //temp
    private val balances = listOf(70.0, 45.0, -115.0)
    //TODO remove balances

    init {
        coroutineScope.launch {
            updateUsers()
        }
    }

    suspend fun updateUsers() {
        runBlocking{ getUsers() }
        users = groupRepository.usersInCurrentGroup.value
        userNames = users.map { "${it.name} ${it.surname}" }
        setupInputFields()
    }

    private suspend fun getUsers() {
        groupRepository.getUsersInCurrentGroup()
    }


    fun setupInputFields() {
        _inputFieldsData.clear()
        _inputFieldsData.addAll(
            userNames.zip(balances).mapIndexed { index, (username, balance) ->
                val user = users[index]
                InputFieldData.UserBalanceButtonData(
                    title = username,
                    balance = balance.toFloat(),
                    onClick = { onUserClicked(currentGroup.value!!, user) }
                )
            }
        )
    }
}