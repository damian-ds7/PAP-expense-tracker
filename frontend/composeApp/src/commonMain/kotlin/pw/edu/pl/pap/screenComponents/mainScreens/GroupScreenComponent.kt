package pw.edu.pl.pap.screenComponents.mainScreens

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.launch
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.data.uiSetup.inputFields.ButtonData
import pw.edu.pl.pap.data.uiSetup.inputFields.InputFieldData
import pw.edu.pl.pap.data.uiSetup.inputFields.UserBalanceButtonData

class GroupScreenComponent(
    private val onUserClicked: (User) -> Unit,
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
    private val balances = listOf(70.0.toFloat(), 45.0.toFloat(), (-115.0).toFloat())
    //TODO fetch balances


    fun setupInputFields() {
        _inputFieldsData.clear()
        _inputFieldsData.addAll(
            userNames.zip(balances).mapIndexed { index, (username, balance) ->
                val user = users[index]
                InputFieldData(
                    title = "",
                    isUserBalanceButton = true,
                    userBalanceButtonData = UserBalanceButtonData(
                        title = username,
                        balance = balance,
                        onClick = { onUserClicked(user) }
                    )
                )
            }
        )
    }
}