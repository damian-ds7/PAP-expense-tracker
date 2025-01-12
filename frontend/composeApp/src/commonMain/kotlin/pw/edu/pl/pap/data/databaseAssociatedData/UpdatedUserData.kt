package pw.edu.pl.pap.data.databaseAssociatedData

import kotlinx.serialization.Serializable

@Serializable
data class UpdatedUserData(
    val name: String,
    val surname: String,
    val email: String,
)