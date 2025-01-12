package pw.edu.pl.pap.screenComponents.mainScreens

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.inject
import pw.edu.pl.pap.data.databaseAssociatedData.Expense
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup
import pw.edu.pl.pap.repositories.data.ExpenseRepository
import pw.edu.pl.pap.repositories.data.GroupRepository
import pw.edu.pl.pap.screenComponents.BaseComponent
import pw.edu.pl.pap.util.sortingSystem.GroupKey

class HomeScreenComponent(
    baseScreenComponent: BaseComponent,
    val onAddExpenseButtonClicked: (UserGroup) -> Unit,
    val onExpenseClick: (Expense) -> Unit
) : BaseComponent by baseScreenComponent {

    sealed class NavigationState {
        data object InitialLoad : NavigationState()
        data object FromNewExpenseScreen : NavigationState()
        data object Empty : NavigationState()
    }

    private val expenseRepository: ExpenseRepository by inject()
    private val groupRepository: GroupRepository by inject()

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.InitialLoad)
    val navigationState: StateFlow<NavigationState> get() = _navigationState

    private val _loadingData = MutableStateFlow(true)
    val loadingData: StateFlow<Boolean> get() = _loadingData

    init {
        if (groupRepository.currentUserGroup.value == null) {
            runBlocking { populateGroupList() }
        }
    }

    fun updateNavigationState(newState: NavigationState) {
        _navigationState.value = newState
    }

    fun getDataBasedOnState() {
        when (_navigationState.value) {
            is NavigationState.InitialLoad -> {
                fetchInitialPageExpenses()
            }

            is NavigationState.FromNewExpenseScreen -> {
                getRecentExpense()
            }

            is NavigationState.Empty -> {
                // Do nothing
            }
        }
        updateNavigationState(NavigationState.Empty)
        fetchHomeInfo()
    }

    val userGroupInfo = groupRepository.allGroups

    val currentUserGroup = groupRepository.currentUserGroup

    fun updateUserGroup(key: UserGroup) {
        groupRepository.updateCurrentGroup(key)
    }

    private suspend fun populateGroupList() {
        groupRepository.getGroups()
    }

    val homeInfo = expenseRepository.homeInfo
    val groupedExpenses = expenseRepository.groupedExpenses
    val moreToLoad = expenseRepository.moreToLoad

    val currentGroupingKey = expenseRepository.currentGroupingKey
    val currentGroupingOrder = expenseRepository.currentGroupingOrder

    fun updateGroupingKey(key: GroupKey) {
        expenseRepository.updateGroupingKey(key)
    }

    private fun fetchHomeInfo() {
        coroutineScope.launch {
            expenseRepository.getTotalExpenses(groupRepository.getCurrentGroupName())
        }
    }

    private fun fetchInitialPageExpenses() {
        _loadingData.value = true
        coroutineScope.launch {
            expenseRepository.loadInitialPage(groupRepository.getCurrentGroupName())
        }.invokeOnCompletion { _loadingData.value = false }
    }

    fun fetchNextPage() {
        _loadingData.value = true
        coroutineScope.launch {
            println("next page")
            expenseRepository.loadNextPage(groupRepository.getCurrentGroupName())
        }.invokeOnCompletion { _loadingData.value = false }
    }

    private fun getRecentExpense() {
        coroutineScope.launch {
            expenseRepository.getRecentExpense(groupRepository.getCurrentGroupName())
        }.invokeOnCompletion { _loadingData.value = false }
    }

    fun sortGroups() {
        expenseRepository.switchGroupingOrder()
        _loadingData.value = false
        coroutineScope.launch {
            expenseRepository.loadInitialPage(groupRepository.getCurrentGroupName())
        }.invokeOnCompletion { _loadingData.value = false }
    }
}