package pw.edu.pl.pap.repositories.data

import pw.edu.pl.pap.api.data.MembershipApi
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup

class MembershipRepository(val api: MembershipApi) {

    suspend fun invite(user: User, group: UserGroup){
        try {
            api.sendInvite(user, group)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}