package pw.edu.pl.pap.data

import kotlinx.serialization.Serializable

@Serializable
data class Record(
    val id: Int,
    val price: Float,
    val user: User,
)
