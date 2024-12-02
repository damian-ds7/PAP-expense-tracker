package pw.edu.pl.pap.data

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class Record(
    val id: Long,
    val price: Float,
    val date: LocalDate,
    val user: User,
    val category: Category,
)
