package pw.edu.pl.pap.data.databaseAssociatedData

import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethod(
    val id: Long,
    val name: String
)
