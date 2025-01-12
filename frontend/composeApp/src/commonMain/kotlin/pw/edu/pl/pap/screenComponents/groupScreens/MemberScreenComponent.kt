package pw.edu.pl.pap.screenComponents.groupScreens

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.inject
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.uiSetup.ConfirmationDialogConfig
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.repositories.data.ConfigRepository
import pw.edu.pl.pap.repositories.data.GroupRepository
import pw.edu.pl.pap.repositories.data.MembershipRepository
import pw.edu.pl.pap.repositories.data.UserRepository
import pw.edu.pl.pap.screenComponents.BaseComponent

class MemberScreenComponent(
    baseComponent: BaseComponent,
    val user: User,
    val onBack: () -> Unit
) : BaseComponent by baseComponent {
    private val userRepository: UserRepository by inject()
    private val configRepository: ConfigRepository by inject()
    private val membershipRepository: MembershipRepository by inject()

    private val groupRepository: GroupRepository by inject()
    private val currentUserGroup = groupRepository.currentUserGroup

    private val isAdmin = userRepository.isAdmin
    private val currentRole = userRepository.currentRole

    init{
        runBlocking {
            userRepository.checkIsAdmin(currentUserGroup.value!!)
            userRepository.getUserRole(currentUserGroup.value!!, user)
        }
    }

    private val _inputFieldsData = mutableStateListOf<InputFieldData>()
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData

    private val roles = configRepository.roles

    private val initialIndex: Int = configRepository.roles.value.indexOf(userRepository.currentRole.value)
    private var roleIndex: MutableState<Int> = mutableStateOf(configRepository.roles.value.indexOf(userRepository.currentRole.value))


    var showChangeRoleConfirmationDialog: MutableState<Boolean> = mutableStateOf(false)
    var showKickConfirmationDialog: MutableState<Boolean> = mutableStateOf(false)
    var showLeaveConfirmationDialog: MutableState<Boolean> = mutableStateOf(false)

    val changeRoleConfirmationData = ConfirmationDialogConfig(
        mainText = "Change Role",
        subText = "Are you sure you want to change the role of \"${user.name} ${user.surname}\"?",
        onNo = { showChangeRoleConfirmationDialog.value = false },
        onYes = {
            showChangeRoleConfirmationDialog.value = false
            coroutineScope.launch { changeRole() }
            onBack()
        }
    )

    val kickConfirmationData = ConfirmationDialogConfig(
        mainText = "Kick",
        subText = "Are you sure you want to kick \"${user.name} ${user.surname}\" from ${groupRepository.getCurrentGroupName()}?\n" +
                  "All expenses paid by this user in ${groupRepository.getCurrentGroupName()} will be gone!",
        onNo = { showKickConfirmationDialog.value = false },
        onYes = {
            showKickConfirmationDialog.value = false
            coroutineScope.launch { kickMember() }
            onBack()
        }
    )

    val leaveConfirmationData = ConfirmationDialogConfig(
        mainText = "Leave",
        subText = "Are you sure you want to leave from ${groupRepository.getCurrentGroupName()}?\n" +
                "All expenses paid by you in ${groupRepository.getCurrentGroupName()} will be gone!",
        onNo = { showLeaveConfirmationDialog.value = false },
        onYes = {
            showLeaveConfirmationDialog.value = false
            coroutineScope.launch { leave() }
            onBack()
        }
    )

    val canConfirm by derivedStateOf { initialIndex != roleIndex.value }

    fun setupInputFields() {
        _inputFieldsData.clear()
        _inputFieldsData.addAll(
            listOf(
                InputFieldData.DropdownListData(
                    title = "Role: ",
                    itemList = roles.value,
                    selectedIndex = roleIndex,
                    onItemClick = if (isAdmin.value) {
                        { coroutineScope.launch { roleIndex.value = it } }
                    } else {
                        {}
                    }
                )
            ) + if (isAdmin.value && user != userRepository.currentUserInfo.value) {
                listOf(
                    InputFieldData.ButtonData(
                        title = "KICK",
                        onClick = {
                            coroutineScope.launch { showKickConfirmationDialog.value = true }
                        },
                        isColored = true,
                    )
                )
            } else if (isAdmin.value && user == userRepository.currentUserInfo.value) {
                listOf(
                    InputFieldData.ButtonData(
                        title = "LEAVE",
                        onClick = {
                            coroutineScope.launch { showLeaveConfirmationDialog.value = true }
                        },
                        isColored = true,
                    )
                )
            } else {
                emptyList()
            }
        )
    }

    private suspend fun changeRole() {
        userRepository.changeRole(currentUserGroup.value!!, user, roles.value[roleIndex.value])
    }

    private suspend fun kickMember() {
        runBlocking { membershipRepository.kickMember(user, currentUserGroup.value!!) }
        groupRepository.getUsersInCurrentGroup()
        //TODO test it
    }

    private suspend fun leave(){
        membershipRepository.kickMember(user, currentUserGroup.value!!)
        groupRepository.updateAfterLeaving()
        //TODO test it
    }
}