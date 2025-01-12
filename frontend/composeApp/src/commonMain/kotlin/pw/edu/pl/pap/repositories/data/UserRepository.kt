package pw.edu.pl.pap.repositories.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.java.KoinJavaComponent.inject
import pw.edu.pl.pap.api.data.UserApi
import pw.edu.pl.pap.data.databaseAssociatedData.NewPassword
import pw.edu.pl.pap.data.databaseAssociatedData.UpdatedUserData
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup

class UserRepository(val api: UserApi) {

    private val _usersFound = MutableStateFlow<List<User>>(listOf())
    val usersFound: StateFlow<List<User>> get() = _usersFound

    private val _isAdmin = MutableStateFlow<Boolean>(false)
    val isAdmin: StateFlow<Boolean> get() = _isAdmin

    private val _currentUserInfo = MutableStateFlow<User?>(null)
    val currentUserInfo : StateFlow<User?> get() = _currentUserInfo



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

    suspend fun updateUser(user: UpdatedUserData){
        try {
            println(api.updateUserResponse(user))
            _currentUserInfo.value = api.updateUser(user)
            //TODO remove print when debugged
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getCurrentUserInfo() {
        _currentUserInfo.value = api.getCurrentUserInfo()
    }

    suspend fun changePassword(newPassword: NewPassword){
        try {
            println(api.changePassword(newPassword))
            //TODO remove print when debugged
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}