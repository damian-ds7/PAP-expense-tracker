package pw.edu.pl.pap.data.databaseAssociatedData

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class NewExpense(
    val price: Float,
    val date: LocalDate,
    val user: User
)