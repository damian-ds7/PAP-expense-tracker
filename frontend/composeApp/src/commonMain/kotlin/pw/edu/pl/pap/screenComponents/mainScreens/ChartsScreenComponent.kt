package pw.edu.pl.pap.screenComponents.mainScreens

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.core.component.inject
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.repositories.data.ChartsRepository
import pw.edu.pl.pap.repositories.data.GroupRepository
import pw.edu.pl.pap.screenComponents.BaseComponent
import pw.edu.pl.pap.util.charts.FilterTimeFrames

class ChartsScreenComponent(
    baseComponent: BaseComponent,
    val onFilterClick: () -> Unit,
) : BaseComponent by baseComponent {

    private val chartsRepository: ChartsRepository by inject()
    private val groupRepository: GroupRepository by inject()

    sealed class NavigationState {
        data object LoadData : NavigationState()
        data object Empty : NavigationState()
    }

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.LoadData)
    val navigationState: StateFlow<NavigationState> get() = _navigationState

    fun updateNavigationState(newState: NavigationState) {
        _navigationState.value = newState
    }

    //TODO refactor or remove
    fun getDataBasedOnState() {
        when (_navigationState.value) {
            is NavigationState.LoadData -> {
                coroutineScope.launch {
                    getPlotData()
                }
            }

            is NavigationState.Empty -> {
                // Do nothing
            }
        }
        _navigationState.value = NavigationState.Empty
    }

    val chartFilters = chartsRepository.chartFilters

    private val keyPattern = chartsRepository.keyPattern

    val plotData = chartsRepository.plotData

    val userGroupInfo = groupRepository.allGroups

    val currentUserGroup = groupRepository.currentUserGroup

    val currentTimeFrame = chartsRepository.currentTimeFrame

    val currentTimeBounds = chartsRepository.currentTimeBounds

    fun updateCurrentUserGroup(key: UserGroup) {
        groupRepository.updateCurrentGroup(key)
        _navigationState.value = NavigationState.LoadData
    }

    private suspend fun getPlotData() {
        chartsRepository.getPlotData(groupRepository.getCurrentGroupName())
    }

    fun getTotal(): Double {
        return plotData.value.values.sumOf { it.toDouble() }
    }

    fun changeTimeFrame(newTimeFrame: FilterTimeFrames) {
        chartsRepository.changeTimeFrame(newTimeFrame)
        _navigationState.value = NavigationState.LoadData
    }

    /**
     * Shifts current time bounds by a given number of months or years
     * depending on current time frame
     *
     * @param amount The amount to change current bounds
     */
    fun modifyTimeBounds(
        amount: Int,
    ) {
        chartsRepository.modifyTimeBounds(amount)
        _navigationState.value = NavigationState.LoadData
    }

    /**
     * Modifies time bounds to custom date range from
     * given begin and end date
     *
     * @param beginDate time bounds start date
     * @param endDate time bounds end date
     */
    fun modifyTimeBounds(beginDate: LocalDate?, endDate: LocalDate?) {
        chartsRepository.modifyTimeBounds(beginDate, endDate)
        _navigationState.value = NavigationState.LoadData
    }
}