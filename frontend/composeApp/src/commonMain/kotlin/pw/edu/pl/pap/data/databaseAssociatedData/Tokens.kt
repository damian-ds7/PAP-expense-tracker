package pw.edu.pl.pap.data.databaseAssociatedData

import kotlinx.serialization.Serializable

@Serializable
data class Tokens(
    val accessToken: String, val refreshToken: String
)

@Serializable
data class RefreshToken(
    val refreshToken: String
)
