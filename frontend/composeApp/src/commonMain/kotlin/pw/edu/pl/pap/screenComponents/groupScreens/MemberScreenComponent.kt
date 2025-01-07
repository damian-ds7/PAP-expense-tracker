package pw.edu.pl.pap.screenComponents.groupScreens

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.data.uiSetup.ConfirmationDialogConfig
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.screenComponents.BaseScreenComponent

class MemberScreenComponent(
    baseComponent: BaseScreenComponent,
    val user: User,
    private val currentUserGroup: UserGroup,
    val onBack: () -> Unit
) : BaseScreenComponent by baseComponent {

    private val _inputFieldsData = mutableStateListOf<InputFieldData>()
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData

    private val roles = listOf("admin", "viewer")

    //TODO fetch list of roles
    private var initialIndex: MutableState<Int> = mutableStateOf(0)
    private var roleIndex: MutableState<Int> = mutableStateOf(0)

    private val isAdmin: Boolean = true
    //TODO fetch if is admin

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
        subText = "Are you sure you want to kick \"${user.name} ${user.surname}\" from ${currentUserGroup.name}?",
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
                    itemList = roles,
                    selectedIndex = roleIndex,
                    onItemClick = if (isAdmin) {
                        { coroutineScope.launch { roleIndex.value = it } }
                    } else {
                        {}
                    }
                )
            ) + if (isAdmin) {
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