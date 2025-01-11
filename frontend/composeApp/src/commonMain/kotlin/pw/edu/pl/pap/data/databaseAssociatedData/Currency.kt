package pw.edu.pl.pap.data.databaseAssociatedData

import kotlinx.serialization.Serializable

@Serializable
data class Currency(
    val id: Long,
    val symbol: String,
    val exchangeRate: Float
)
