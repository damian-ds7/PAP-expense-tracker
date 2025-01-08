package pw.edu.pl.pap.api.endpoints

import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup

sealed class TemporaryMembershipEndpoint(relativePath: String) : BaseEndpoint("/tempMembership", relativePath) {
    data class Accept(val id: Long): TemporaryMembershipEndpoint("/accept/$id")
    data class Decline(val id: Long): TemporaryMembershipEndpoint("/delete/$id")
    data object GetReceived: TemporaryMembershipEndpoint("/invitations")
    data object GetSent: TemporaryMembershipEndpoint("/invitations")
    //TODO correct it
}
