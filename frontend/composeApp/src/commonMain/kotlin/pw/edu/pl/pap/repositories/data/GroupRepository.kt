package pw.edu.pl.pap.repositories.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

//    private lateinit var users: Pair<UserGroup, List<User>>

    fun updateCurrentGroup(key: UserGroup) {
        _currentUserGroup.value = key
    }

    suspend fun getGroups(force: Boolean = false) {
        if (_currentUserGroup.value == null || force) {
            try {
                val allGroups = api.getUserGroups()
                _allGroups.value = allGroups
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
            getGroups()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteGroup(group: UserGroup) {
        try {
            api.deleteGroup(group.id)
            getGroups()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun addGroup(group: NewGroup) {
        try {
            api.postNewGroup(group)
            getGroups()
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
}