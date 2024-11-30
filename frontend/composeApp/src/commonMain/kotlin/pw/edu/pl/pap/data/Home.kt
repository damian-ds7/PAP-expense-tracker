package pw.edu.pl.pap.data

import kotlinx.serialization.Serializable


@Serializable
data class Home(
    val userExpenses: Double? = null,
    val expenses: Double? = null,
)