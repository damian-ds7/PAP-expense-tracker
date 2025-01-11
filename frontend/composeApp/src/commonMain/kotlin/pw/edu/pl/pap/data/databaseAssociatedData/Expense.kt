package pw.edu.pl.pap.data.databaseAssociatedData

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class Expense(
    val id: Long,
    val title: String,
    val price: Float,
    val expenseDate: LocalDate,
    val category: Category,
    val methodOfPayment: PaymentMethod,
    val currency: Currency,
    val user: User,
    val groupName: String
)
