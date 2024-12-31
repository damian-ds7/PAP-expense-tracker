package pw.edu.pl.pap.screenComponents.mainScreens

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pw.edu.pl.pap.data.databaseAssociatedData.ChartFilterParams
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.util.charts.ChartData
import java.util.TreeMap

class ChartsScreenComponent(
    baseComponent: BaseScreenComponent,
) : BaseScreenComponent by baseComponent {

    sealed class NavigationState {
        data object InitialLoad : NavigationState()
        data object Empty : NavigationState()
    }

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.InitialLoad)
    val navigationState: StateFlow<NavigationState> get() = _navigationState

    fun getDataBasedOnState() {
        when (_navigationState.value) {
            is NavigationState.InitialLoad -> {
                getGroups()
                getChartData()
            }
            is NavigationState.Empty -> {
                // Do nothing
            }
        }
    }

    private val _chartFilters = MutableStateFlow(ChartFilterParams())
    val chartFilters: StateFlow<ChartFilterParams> get() = _chartFilters

    private val _plotData = MutableStateFlow<ChartData>(TreeMap())
    val plotData: StateFlow<ChartData> get() = _plotData

    private val _userGroupInfo = MutableStateFlow<List<UserGroup>?>(emptyList())
    val userGroupInfo: StateFlow<List<UserGroup>?> get() = _userGroupInfo

    private val _currentUserGroup = MutableStateFlow<UserGroup?>(null)
    val currentUserGroup: StateFlow<UserGroup?> get() = _currentUserGroup

    private suspend fun populateGroupList() {
        when (_userGroupInfo.value) {
            null -> {
                val userGroupInfo = apiService.groupApiClient.getUserGroups()
                _userGroupInfo.value = userGroupInfo
                _currentUserGroup.value = _userGroupInfo.value?.first()
            }
            else -> return
        }
    }

    private fun getGroups() {
        coroutineScope.launch {
            populateGroupList()
            println(_userGroupInfo.value)
        }
    }

    private fun getChartData() {
        coroutineScope.launch {
            _plotData.value = apiService.chartsApiClient.getGroupYearData(2024)
            println(_plotData.value)
        }
    }

}