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

    init{
        runBlocking {
            userRepository.checkIsAdmin(currentUserGroup.value!!)
        }
    }

    private val _inputFieldsData = mutableStateListOf<InputFieldData>()
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData

    private val roles = configRepository.roles

    //TODO fetch roles
    private var initialIndex: MutableState<Int> = mutableStateOf(0)
    private var roleIndex: MutableState<Int> = mutableStateOf(0)



    var showChangeRoleConfirmationDialog: MutableState<Boolean> = mutableStateOf(false)
    var showKickConfirmationDialog: MutableState<Boolean> = mutableStateOf(false)

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
        subText = "Are you sure you want to kick \"${user.name} ${user.surname}\" from ${groupRepository.getCurrentGroupName()}?",
        onNo = { showChangeRoleConfirmationDialog.value = false },
        onYes = {
            showChangeRoleConfirmationDialog.value = false
            coroutineScope.launch { kick() }
            onBack()
        }
    )

    val canConfirm by derivedStateOf { initialIndex.value != roleIndex.value }

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
            ) + if (isAdmin.value) {
                listOf(
                    InputFieldData.ButtonData(
                        title = "KICK",
                        onClick = {
                            coroutineScope.launch { showKickConfirmationDialog.value = true }
                        },
                        isColored = true,
                    )
                )
            } else {
                emptyList()
            }
        )
    }

    private fun changeRole() {
        //TODO
    }

    private fun kick() {
        //TODO
    }
}