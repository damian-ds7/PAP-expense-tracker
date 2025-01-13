package pw.edu.pl.pap.repositories.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pw.edu.pl.pap.api.data.TemporaryMembershipApi
import pw.edu.pl.pap.data.databaseAssociatedData.DateKeyExpensePage
import pw.edu.pl.pap.data.databaseAssociatedData.Invitation
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.databaseAssociatedData.toMap

class TemporaryMembershipRepository(val api: TemporaryMembershipApi) {

    private val _invitationsReceived = MutableStateFlow<List<Invitation>>(listOf())
    val invitationsReceived: StateFlow<List<Invitation>> get() = _invitationsReceived

    private val _invitationsSent = MutableStateFlow<List<Invitation>>(listOf())
    val invitationsSent: StateFlow<List<Invitation>> get() = _invitationsSent

    private suspend fun getInvitationsReceived() {
        _invitationsReceived.value = api.getReceived()
    }

    private suspend fun getInvitationsSent() {
        _invitationsSent.value = api.getSent()
    }

    suspend fun getPendingInvitations(){
        getInvitationsReceived()
        getInvitationsSent()
    }

    suspend fun accept(id: Long){
        try {
            println(api.accept(id))
            getPendingInvitations()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun decline(id: Long){
        try {
            println(api.decline(id))
            getPendingInvitations()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}