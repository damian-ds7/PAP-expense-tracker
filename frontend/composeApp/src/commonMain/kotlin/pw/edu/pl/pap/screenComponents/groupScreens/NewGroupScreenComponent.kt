package pw.edu.pl.pap.screenComponents.groupScreens

import kotlinx.coroutines.runBlocking
import pw.edu.pl.pap.data.databaseAssociatedData.NewGroup
import pw.edu.pl.pap.screenComponents.BaseScreenComponent

class NewGroupScreenComponent (
    baseComponent: BaseScreenComponent,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
//    private val currentUserGroup: UserGroup,
) : BaseGroupEditScreenComponent(baseComponent, onDismiss, onSave) {

    override fun confirm() {
        val newGroup = NewGroup(name.value)

        runBlocking{
            apiService.groupApiClient.postNewGroup(newGroup)
            onSave()
        }
    }
}