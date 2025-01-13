package pw.edu.pl.pap.repositories.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent.inject
import pw.edu.pl.pap.api.data.GroupApi
import pw.edu.pl.pap.data.databaseAssociatedData.NewGroup
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup

class GroupRepository(val api: GroupApi) {

    private val _allGroups = MutableStateFlow<List<UserGroup>>(listOf())
    val allGroups: StateFlow<List<UserGroup>> get() = _allGroups

    private val _currentUserGroup = MutableStateFlow<UserGroup?>(null)
    val currentUserGroup: StateFlow<UserGroup?> get() = _currentUserGroup

    private val _usersInCurrentGroup = MutableStateFlow<List<User>>(listOf())
    val usersInCurrentGroup : StateFlow<List<User>> get() = _usersInCurrentGroup

    fun updateCurrentGroup(key: UserGroup) {
        _currentUserGroup.value = key
        runBlocking { getUsersInCurrentGroup() }
    }



    suspend fun getGroups(force: Boolean = false) {
        if (_currentUserGroup.value == null || force) {
            try {
                val allGroups = api.getUserGroups()
                _allGroups.value = allGroups
                print("From getgroup: " + allGroups)
                if (_currentUserGroup.value == null) {
                    _currentUserGroup.value = _allGroups.value.first()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getCurrentGroupName(): String {
        return currentUserGroup.value?.name.orEmpty()
    }

    suspend fun updateGroup(group: UserGroup) {
        try {
            api.updateGroup(group.id, group)
            _currentUserGroup.value = group
            getGroups(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteGroup(group: UserGroup) {
        try {
            println(api.deleteGroup(group.id))
            _currentUserGroup.value = null
            getGroups()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun addGroup(group: NewGroup) {
        try {
            api.postNewGroup(group)
            getGroups(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getUsersInCurrentGroup() {
        try {
            _usersInCurrentGroup.value = api.getUsersInGroup(currentUserGroup.value!!.name)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun refreshGroups() {
        getGroups(true)
        println(_allGroups.value)
        println(_currentUserGroup.value)
        if (_allGroups.value.isEmpty()) {
            _currentUserGroup.value = null
        } else if (_currentUserGroup.value !in _allGroups.value) {
            _currentUserGroup.value = _allGroups.value.first()
        }
        println(_allGroups.value)
        println(_currentUserGroup.value)
        getUsersInCurrentGroup()
    }
}