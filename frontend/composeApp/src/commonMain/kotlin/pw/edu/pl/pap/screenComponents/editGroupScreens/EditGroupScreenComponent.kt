package pw.edu.pl.pap.screenComponents.editGroupScreens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import io.ktor.http.*
import kotlinx.coroutines.launch
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.screenComponents.mainScreens.BaseScreenComponent

class EditGroupScreenComponent(
    baseComponent: BaseScreenComponent,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    private val onDelete: () -> Unit,
    private val group: UserGroup
) : BaseGroupEditScreenComponent(baseComponent, onDismiss, onSave) {

    override var name: MutableState<String> = mutableStateOf(group.name)

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

    fun deleteExpense() {
        println("Deleting group $group")
        coroutineScope.launch {
            if (apiService.groupApiClient.deleteGroup(group.id).status.isSuccess()) {
                onDelete()
            }
        }
    }
}