package pw.edu.pl.pap.api.endpoints

import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup

sealed class MembershipEndpoint(relativePath: String) : BaseEndpoint("/membership", relativePath) {
    data class Invite(val user: User, val group: UserGroup) : MembershipEndpoint("/invite")
}