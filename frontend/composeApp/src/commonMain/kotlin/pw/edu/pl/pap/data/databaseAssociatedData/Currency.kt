package pw.edu.pl.pap.data.databaseAssociatedData

import kotlinx.serialization.Serializable

@Serializable
data class Currency(
    val id: Long,
    val name: String,
    val symbol: String,
    val exchangeRate: Float
)
