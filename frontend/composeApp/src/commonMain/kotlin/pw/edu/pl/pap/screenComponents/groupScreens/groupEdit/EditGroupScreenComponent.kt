package pw.edu.pl.pap.screenComponents.groupScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pw.edu.pl.pap.data.uiSetup.ConfirmationDialogConfig
import pw.edu.pl.pap.screenComponents.BaseComponent

class EditGroupScreenComponent(
    baseComponent: BaseComponent,
    onDismiss: () -> Unit,
) : BaseGroupEditScreenComponent(baseComponent, onDismiss) {

    override var name: MutableState<String> = mutableStateOf(groupRepository.getCurrentGroupName())

    val noChange by derivedStateOf { canConfirm && name.value == name.value }

    var showDeleteGroupConfirmationDialog: MutableState<Boolean> = mutableStateOf(false)

    val deleteGroupConfirmationData = ConfirmationDialogConfig(
        mainText = "Delete group",
        subText = "Are you sure you want to delete group ${groupRepository.getCurrentGroupName()}?\n" +
                  "All expenses in ${groupRepository.getCurrentGroupName()} will be gone!",
        onNo = { showDeleteGroupConfirmationDialog.value = false },
        onYes = {
            showDeleteGroupConfirmationDialog.value = false
            coroutineScope.launch { deleteGroup() }
        }
    )

    override fun confirm() {
        val newGroup = group?.copy(name = name.value)!!

        if (newGroup == group) {
            onDismiss()
            return
        }

        coroutineScope.launch {
            groupRepository.updateGroup(newGroup)
            onDismiss()
        }

        println("Updated Group ${newGroup.id} from ${group.name} to ${newGroup.name}")
    }

    fun deleteGroup() {
        println("Deleting group $group")
        runBlocking { groupRepository.deleteGroup(group!!) }
        onDismiss()
    }
}