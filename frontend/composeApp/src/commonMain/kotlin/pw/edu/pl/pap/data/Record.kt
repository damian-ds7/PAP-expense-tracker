package pw.edu.pl.pap.data

import kotlinx.serialization.Serializable

@Serializable
data class Record(
    val id: Long,
    val price: Float,
    val date: String,
    val user: User,
    val category: Category,
)
