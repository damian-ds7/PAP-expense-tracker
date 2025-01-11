package pw.edu.pl.pap.data.databaseAssociatedData

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class NewExpense(
    val title: String,
    val price: Float,
    val user: User,
    val groupName: String,
    val categoryName: String,
    val expenseDate: LocalDate,
    val methodOfPayment: String,
    val currencyCode: String,
)