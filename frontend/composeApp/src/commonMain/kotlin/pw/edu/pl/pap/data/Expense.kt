package pw.edu.pl.pap.data

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class Expense(
    val id: Long,
    val price: Float,
    val user: User,
    val date: LocalDate,
    val category: Category,
)
