package pw.edu.pl.pap.data.databaseAssociatedData

import kotlinx.serialization.Serializable

@Serializable
data class UserGroup (
    val id: Int,
    val name: String,
)
