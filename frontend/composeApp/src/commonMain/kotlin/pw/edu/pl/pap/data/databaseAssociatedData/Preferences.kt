package pw.edu.pl.pap.data.databaseAssociatedData

import kotlinx.serialization.Serializable

@Serializable
data class Preferences (
    val currencySymbol: String,
    val methodOfPayment: String,
    val language: String
)