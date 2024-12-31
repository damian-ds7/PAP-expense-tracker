package pw.edu.pl.pap.data.databaseAssociatedData

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class ChartFilterParams(
    val group: Long? = null,
    val people: List<Long>? = null,
    val categories: List<Long>? = null,
    val period: Pair<LocalDate, LocalDate>? = null,
    val year: Int? = null
)
