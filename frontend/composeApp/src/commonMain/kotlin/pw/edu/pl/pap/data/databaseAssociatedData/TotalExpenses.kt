package pw.edu.pl.pap.data.databaseAssociatedData

import kotlinx.serialization.Serializable


@Serializable
data class TotalExpenses(
    val groupExpenses: Float = 0f,
    val userExpenses: Float = 0f,
)