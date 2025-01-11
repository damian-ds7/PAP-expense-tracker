package pw.edu.pl.pap.data.databaseAssociatedData

import kotlinx.serialization.Serializable
import pw.edu.pl.pap.util.sortingSystem.GroupMapKey


interface ExpensePage {
    val data: LinkedHashMap<out GroupMapKey, List<Expense>>
    val nextLastId: Long
    val nextLastKey: String
    val hasMore: Boolean
}
@Serializable
data class DateKeyExpensePage(
    override val data: LinkedHashMap<GroupMapKey.DateKey, List<Expense>>,
    override val nextLastId: Long,
    override val nextLastKey: String,
    override val hasMore: Boolean
) : ExpensePage

@Serializable
data class StringKeyExpensePage(
    override val data: LinkedHashMap<GroupMapKey.StringKey, List<Expense>>,
    override val nextLastId: Long,
    override val nextLastKey: String,
    override val hasMore: Boolean
) : ExpensePage