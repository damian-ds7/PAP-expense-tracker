package pw.edu.pl.pap.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
)
