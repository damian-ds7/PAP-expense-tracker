package pw.edu.pl.pap.data

import kotlinx.serialization.Serializable

@Serializable
data class UserSignUpData(
    val name: String,
    val surname: String,
    val email: String,
    val password: String
)

