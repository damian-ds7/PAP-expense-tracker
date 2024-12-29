package pw.edu.pl.pap.data.databaseAssociatedData

import kotlinx.serialization.Serializable

@Serializable
data class UserGroup (
    val id: Int? = null,
    val name: String? = null,
)
