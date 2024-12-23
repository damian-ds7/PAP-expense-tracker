package pw.edu.pl.pap.data.databaseAssociatedData

import kotlinx.serialization.Serializable


@Serializable
data class TotalExpenses(
    val groupExpenses: Float? = null,
    val userExpenses: Float? = null,
)