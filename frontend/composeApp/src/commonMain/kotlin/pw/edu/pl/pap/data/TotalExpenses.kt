package pw.edu.pl.pap.data

import kotlinx.serialization.Serializable


@Serializable
data class TotalExpenses(
    val groupExpenses: Double? = null,
    val userExpenses: Double? = null,
)