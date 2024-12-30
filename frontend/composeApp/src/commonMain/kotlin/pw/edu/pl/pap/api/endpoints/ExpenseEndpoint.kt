package pw.edu.pl.pap.api.endpoints

sealed class ExpenseEndpoint(private val relativePath: String) : ApiEndpoint {
    companion object {
        private const val BASE_PATH = "/expense"
    }

    override val path: String
        get() = "$BASE_PATH$relativePath"

    data object RecentExpense : ExpenseEndpoint("/recent")
    data object NewExpense : ExpenseEndpoint("/insert")
    data class DeleteExpense(val id: Number) : ExpenseEndpoint("/delete/$id")
    data class ExpenseById(val id: Number) : ExpenseEndpoint("/$id")
    data class UpdateExpense(val id: Number) : ExpenseEndpoint("/update/$id")
    data class TotalGroupExpense(val group: String) : ExpenseEndpoint("/state/$group")
    data class GroupDateMap(val group: String) : ExpenseEndpoint("/dateMap/group/$group")
    data class GroupCatMap(val group: String) : ExpenseEndpoint("/categoryMap/group/$group")
}