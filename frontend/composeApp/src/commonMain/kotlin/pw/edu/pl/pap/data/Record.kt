package pw.edu.pl.pap.data

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class Record(
    val id: Long,
    val price: Float,
    val user: User,
    val date: LocalDate,
    val category: Category,
)
