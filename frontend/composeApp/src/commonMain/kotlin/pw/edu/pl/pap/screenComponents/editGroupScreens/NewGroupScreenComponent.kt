package pw.edu.pl.pap.screenComponents.editGroupScreens

import kotlinx.coroutines.runBlocking
import pw.edu.pl.pap.data.databaseAssociatedData.NewExpense
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.screenComponents.mainScreens.BaseScreenComponent
import pw.edu.pl.pap.screenComponents.singleExpense.BaseExpenseScreenComponent

class NewGroupScreenComponent (
    baseComponent: BaseScreenComponent,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    private val currentUserGroup: UserGroup,
) : BaseGroupEditScreenComponent(baseComponent, onDismiss, onSave) {

    override fun confirm() {

        runBlocking{
//            apiService.expenseApiClient.postNewGroup(name.value)
//            onSave()
            //TODO
        }
    }
}