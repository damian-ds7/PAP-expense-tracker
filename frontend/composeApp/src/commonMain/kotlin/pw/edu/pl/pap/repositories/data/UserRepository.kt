package pw.edu.pl.pap.repositories.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pw.edu.pl.pap.api.data.UserApi
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup

class UserRepository(val api: UserApi) {

    private val _usersFound = MutableStateFlow<List<User>>(listOf())
    val usersFound: StateFlow<List<User>> get() = _usersFound

    private val _isAdmin = MutableStateFlow<Boolean>(false)
    val isAdmin: StateFlow<Boolean> get() = _isAdmin



    suspend fun searchUsers(group: UserGroup, name: String, surname:String){
        try {
            _usersFound.value = api.searchUsers(group.name, listOf(name, surname))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun checkIsAdmin(group: UserGroup){
        try {
           _isAdmin.value = api.isAdmin(group.name)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}