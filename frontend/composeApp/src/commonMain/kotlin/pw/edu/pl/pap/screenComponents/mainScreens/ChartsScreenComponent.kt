package pw.edu.pl.pap.screenComponents.mainScreens

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import pw.edu.pl.pap.data.databaseAssociatedData.ChartFilterParams
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.util.charts.ChartData
import pw.edu.pl.pap.util.charts.FilterTimeFrames
import pw.edu.pl.pap.util.dateFunctions.getCurrentDate
import pw.edu.pl.pap.util.dateFunctions.getFirstAndLastDayOfAMonth
import pw.edu.pl.pap.util.dateFunctions.getFirstAndLastDayOfAYear
import java.util.*

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
                coroutineScope.launch {
                    populateGroupList()
                    getPlotData()
                }
            }

            is NavigationState.Empty -> {
                // Do nothing
            }
        }
        _navigationState.value = NavigationState.Empty
    }

    private val _chartFilters = MutableStateFlow(ChartFilterParams())
    val chartFilters: StateFlow<ChartFilterParams> get() = _chartFilters

    private val _plotData = MutableStateFlow<ChartData>(TreeMap())
    val plotData: StateFlow<ChartData> get() = _plotData

    private val _userGroupInfo = MutableStateFlow<List<UserGroup>>(emptyList())
    val userGroupInfo: StateFlow<List<UserGroup>> get() = _userGroupInfo

    private val _currentUserGroup = MutableStateFlow<UserGroup?>(null)
    val currentUserGroup: StateFlow<UserGroup?> get() = _currentUserGroup

    private val _currentTimeFrame = MutableStateFlow(FilterTimeFrames.MONTH)
    val currentTimeFrame: StateFlow<FilterTimeFrames> get() = _currentTimeFrame

    private val _currentTimeBounds = MutableStateFlow<Pair<LocalDate?, LocalDate?>>(
        getFirstAndLastDayOfAMonth(
            getCurrentDate()
        )
    )
    val currentTimeBounds: StateFlow<Pair<LocalDate?, LocalDate?>> get() = _currentTimeBounds

    fun updateCurrentUserGroup(key: UserGroup) {
        _currentUserGroup.value = key
        _navigationState.value = NavigationState.InitialLoad
    }

    private suspend fun populateGroupList() {
        when {
            _userGroupInfo.value.isEmpty() -> {
                val userGroupInfo = apiService.groupApiClient.getUserGroups()
                _userGroupInfo.value = userGroupInfo
                _currentUserGroup.value = _userGroupInfo.value.first()
                println(_userGroupInfo.value)
                println(_currentUserGroup.value)
            }

            else -> return
        }
    }

    private suspend fun getPlotData() {
        val beginDate = currentTimeBounds.value.first
        val endDate = currentTimeBounds.value.second

        _chartFilters.value = _chartFilters.value.copy(beginDate = beginDate, endDate = endDate)

        println("get plot data")
        _plotData.value = apiService.chartsApiClient.getData(
            _currentUserGroup.value!!.name,
            "category",
            _chartFilters.value
        )
        println(_plotData.value)
    }

    fun getTotal(): Number {
        //TODO
        return 0
    }

    fun changeTimeFrame(newTimeFrame: FilterTimeFrames) {
        when (newTimeFrame) {
            FilterTimeFrames.MONTH -> {
                _currentTimeBounds.value = getFirstAndLastDayOfAMonth(getCurrentDate())
            }

            FilterTimeFrames.YEAR -> {
                _currentTimeBounds.value = getFirstAndLastDayOfAYear(getCurrentDate())
            }

            FilterTimeFrames.CUSTOM -> {
                _currentTimeBounds.value = Pair(null, null)
            }
        }
        _currentTimeFrame.value = newTimeFrame
        _navigationState.value = NavigationState.InitialLoad
    }

    fun changeTimeBounds(
        operation: (LocalDate, Int, DateTimeUnit) -> LocalDate,
    ) {
        when (_currentTimeFrame.value) {
            FilterTimeFrames.MONTH -> {
                val curDate = _currentTimeBounds.value.first!!
                val newDate = operation(curDate, 1, DateTimeUnit.MONTH)
                _currentTimeBounds.value = getFirstAndLastDayOfAMonth(newDate)
            }
            FilterTimeFrames.YEAR -> {
                val curDate = _currentTimeBounds.value.first!!
                val newDate = operation(curDate, 1, DateTimeUnit.YEAR)
                _currentTimeBounds.value = getFirstAndLastDayOfAYear(newDate)
            }
            FilterTimeFrames.CUSTOM -> return
        }
        _navigationState.value = NavigationState.InitialLoad
    }
}