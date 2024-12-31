package pw.edu.pl.pap.screenComponents.mainScreens

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.util.charts.ChartData
import java.util.TreeMap

class ChartsScreenComponent(
    baseComponent: BaseScreenComponent,
) : BaseScreenComponent by baseComponent {

    private val _plotData = MutableStateFlow<ChartData>(TreeMap())

    private val _userGroupInfo = MutableStateFlow<List<UserGroup>?>(emptyList())
    val userGroupInfo: StateFlow<List<UserGroup>?> get() = _userGroupInfo

    private val _currentUserGroup = MutableStateFlow<UserGroup?>(null)
    val currentUserGroup: StateFlow<UserGroup?> get() = _currentUserGroup
}