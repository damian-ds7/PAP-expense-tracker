package pw.edu.pl.pap.data.databaseAssociatedData

import kotlinx.serialization.Serializable

@Serializable
data class InviteRequest (
    val user: User,
    val group: UserGroup
)