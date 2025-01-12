package pw.edu.pl.pap.screenComponents.groupScreens

import kotlinx.coroutines.launch
import pw.edu.pl.pap.data.databaseAssociatedData.NewGroup
import pw.edu.pl.pap.screenComponents.BaseComponent

class NewGroupScreenComponent(
    baseComponent: BaseComponent,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
) : BaseGroupEditScreenComponent(baseComponent, onDismiss, onSave) {

    override fun confirm() {
        val newGroup = NewGroup(name.value)

        coroutineScope.launch {
            groupRepository.addGroup(newGroup)
            onDismiss()
        }
    }
}