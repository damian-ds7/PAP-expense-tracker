package pw.edu.pl.pap.data.databaseAssociatedData

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlin.reflect.KProperty1

abstract class FilterParams() {}

@Serializable
data class ChartFilterParams(
    val beginDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val categoryNames: List<String>? = null,
    val emails: List<String>? = null,
    val methods: List<String>? = null
) : FilterParams()

@Serializable
data class NextExpensePageInfo(
    val lastId: Long? = null,
    val lastKey: String? = null,
    val size: Int = 20,
    var order: String
) : FilterParams()

@Serializable
data class UserSearch(
    val name: String? = null,
    val surname: String? = null,
) : FilterParams()

fun FilterParams.toMap(): Map<String, String?> {
    val result = mutableMapOf<String, String?>()

    this::class.members.filterIsInstance<KProperty1<FilterParams, *>>().forEach { property ->
        val key = property.name
        val value = property.get(this)?.toString()
        result[key] = value
    }

    return result
}