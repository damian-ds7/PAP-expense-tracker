package pw.edu.pl.pap.screenComponents.groupScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import io.ktor.http.*
import kotlinx.coroutines.launch
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.screenComponents.BaseScreenComponent
import pw.edu.pl.pap.util.formatForTextField

class EditGroupScreenComponent(
    baseComponent: BaseScreenComponent,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    private val onDelete: () -> Unit,
    private val group: UserGroup
) : BaseGroupEditScreenComponent(baseComponent, onDismiss, onSave) {

    override var name: MutableState<String> = mutableStateOf(group.name)

    val noChange by derivedStateOf { canConfirm && name.value == group.name }

    override fun confirm() {
        val newGroup = group.copy(name = name.value)

        if (newGroup == group) {
            onDismiss()
            return
        }

        coroutineScope.launch {
            if (apiService.groupApiClient.updateGroup(newGroup).status.isSuccess()) {
                onSave()
            }
        }

        println("Updated Group ${newGroup.id} from ${group.name} to ${newGroup.name}")
    }

    fun deleteGroup() {
        println("Deleting group $group")
        coroutineScope.launch {
            if (apiService.groupApiClient.deleteGroup(group.id).status.isSuccess()) {
                onDelete()
            }
        }
    }
}