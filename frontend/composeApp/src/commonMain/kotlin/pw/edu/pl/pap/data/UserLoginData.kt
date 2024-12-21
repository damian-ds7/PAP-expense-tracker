package pw.edu.pl.pap.data

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginData(
    val email: String,
    val password: String
)
