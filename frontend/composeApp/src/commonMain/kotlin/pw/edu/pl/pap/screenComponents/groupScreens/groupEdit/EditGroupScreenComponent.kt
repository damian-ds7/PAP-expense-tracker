package pw.edu.pl.pap.screenComponents.groupScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pw.edu.pl.pap.screenComponents.BaseComponent

class EditGroupScreenComponent(
    baseComponent: BaseComponent,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    private val onDelete: () -> Unit,
) : BaseGroupEditScreenComponent(baseComponent, onDismiss, onSave) {

    override var name: MutableState<String> = mutableStateOf(groupRepository.getCurrentGroupName())

    val noChange by derivedStateOf { canConfirm && name.value == name.value }

    //TODO add confirmation popup to delete

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
        coroutineScope.launch {
            runBlocking { groupRepository.deleteGroup(group!!) }
            onDismiss()
        }
    }
}