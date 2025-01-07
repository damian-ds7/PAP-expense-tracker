package pw.edu.pl.pap.screenComponents.mainScreens

import androidx.compose.runtime.mutableStateListOf
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.screenComponents.BaseScreenComponent

class GroupScreenComponent(
    private val onUserClicked: (UserGroup, User) -> Unit,
    val onEditGroupClicked: (UserGroup) -> Unit,
    val onNewGroupClicked: () -> Unit,
    val onInvitationsClicked: (UserGroup) -> Unit,
    val currentUserGroup: UserGroup,
    baseComponent: BaseScreenComponent,
) : BaseScreenComponent by baseComponent {

    private val _inputFieldsData = mutableStateListOf<InputFieldData>()
    val inputFieldsData: List<InputFieldData> get() = _inputFieldsData

    //temp
    private val users = listOf(
        User(1, "Herkules", "1", "Kaczka2137@gmail.com"),
        User(2, "Zeus", "2", "Kaczka2137@gmail.com"),
        User(3, "Posejdon", "3", "Kaczka2137@gmail.com"),
    )
    private val userNames = users.map { "${it.name} ${it.surname}" }
    //TODO fetch available users

    //temp
    private val balances = listOf(70.0, 45.0, -115.0)
    //TODO fetch balances


    fun setupInputFields() {
        _inputFieldsData.clear()
        _inputFieldsData.addAll(
            userNames.zip(balances).mapIndexed { index, (username, balance) ->
                val user = users[index]
                InputFieldData.UserBalanceButtonData(
                    title = username,
                    balance = balance.toFloat(),
                    onClick = { onUserClicked(currentUserGroup, user) }
                )
            }
        )
    }
}