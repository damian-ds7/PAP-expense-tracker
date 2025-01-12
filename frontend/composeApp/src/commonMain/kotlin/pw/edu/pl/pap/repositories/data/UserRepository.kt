package pw.edu.pl.pap.repositories.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.java.KoinJavaComponent.inject
import pw.edu.pl.pap.api.data.UserApi
import pw.edu.pl.pap.data.databaseAssociatedData.*

class UserRepository(val api: UserApi) {

    private val _usersFound = MutableStateFlow<List<User>>(listOf())
    val usersFound: StateFlow<List<User>> get() = _usersFound

    private val _isAdmin = MutableStateFlow<Boolean>(false)
    val isAdmin: StateFlow<Boolean> get() = _isAdmin

    private val _currentUserInfo = MutableStateFlow<User?>(null)
    val currentUserInfo : StateFlow<User?> get() = _currentUserInfo

    private val _currentPreferences = MutableStateFlow<Preferences?>(null)
    val currentPreferences : StateFlow<Preferences?> get() = _currentPreferences



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
            _currentUserInfo.value = api.updateUser(user)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getCurrentUserInfo() {
        _currentUserInfo.value = api.getCurrentUserInfo()
    }

    suspend fun changePassword(newPassword: NewPassword){
        try {
            api.changePassword(newPassword)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getCurrentPreferences() {
        _currentPreferences.value = api.getCurrentPreferences()
    }

    suspend fun updatePreferences(newPreferences: Preferences){
        try {
            _currentPreferences.value = api.updatePreferences(newPreferences)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteUser() {
        try {
            api.deleteUser(currentUserInfo.value!!.id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}