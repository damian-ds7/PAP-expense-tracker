package pw.edu.pl.pap.api

import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.coroutines.flow.flow
import pw.edu.pl.pap.api.endpoints.ExpenseEndpoint
import pw.edu.pl.pap.data.databaseAssociatedData.Expense
import pw.edu.pl.pap.data.databaseAssociatedData.NewExpense
import pw.edu.pl.pap.data.databaseAssociatedData.TotalExpenses
import pw.edu.pl.pap.util.sortingSystem.ExpenseMap
import pw.edu.pl.pap.util.sortingSystem.GroupMapKey
import pw.edu.pl.pap.util.sortingSystem.Order

class ExpenseApiClient(baseApiClient: BaseApiClient) :
    ApiClient by baseApiClient {

//    suspend fun getTotalExpenses(): TotalExpenses {
//        return get("state").body()
//    }
//
//    private suspend fun getExpenseDateMapApi(): ExpenseMap {
//        val originalMap: Map<GroupMapKey.DateKey, List<Expense>> = get("all/dateMap").body()
//        return originalMap.toMap(ExpenseMap(initialGroupingOrder = Order.DESCENDING))
//    }
//
//    fun getExpenseDateMap() = flow {
//        emit(getExpenseDateMapApi())
//    }
//
//    private suspend fun getExpenseCatMapApi(): ExpenseMap {
//        val originalMap: Map<GroupMapKey.StringKey, List<Expense>> = get("all/categoryMap").body()
//        return originalMap.toMap(ExpenseMap(initialGroupingOrder = Order.ASCENDING))
//    }
//
//    fun getExpenseCatMap() = flow {
//        emit(getExpenseCatMapApi())
//    }

    private suspend fun getExpenseApi(id: Long): Expense {
        return get(ExpenseEndpoint.ExpenseById(id)).body()
    }

    fun getExpense(id: Long) = flow {
        emit(getExpenseApi(id))
    }

    private suspend fun getRecentExpenseApi(): Expense {
        return get(ExpenseEndpoint.RecentExpense).body()
    }

    fun getRecentExpense() = flow {
        emit(getRecentExpenseApi())
    }

    suspend fun updateExpense(expense: Expense): HttpResponse {
        return put(ExpenseEndpoint.UpdateExpense(expense.id), expense)
    }

    suspend fun postNewExpense(newExpense: NewExpense) {
        println("expense to be uploaded  $newExpense")

        val response: HttpResponse = post(ExpenseEndpoint.NewExpense, newExpense)

        println("Response  " + response.body())
    }

    suspend fun deleteExpense(id: Long): HttpResponse {
        return delete(ExpenseEndpoint.DeleteExpense(id))
    }

    private suspend fun getTotalExpensesForGroupApi(group: String): TotalExpenses {
        return get(ExpenseEndpoint.TotalGroupExpense(group)).body()
    }

    fun getTotalExpensesForGroup(group: String?) = flow {
        emit(getTotalExpensesForGroupApi(group!!))
    }

    private suspend fun getExpenseDateMapForGroupApi(group: String): ExpenseMap {
        val originalMap: Map<GroupMapKey.DateKey, List<Expense>> = get(ExpenseEndpoint.GroupDateMap(group)).body()
        return originalMap.toMap(ExpenseMap(initialGroupingOrder = Order.DESCENDING))
    }

    fun getExpenseDateMapForGroup(group: String?) = flow {
        emit(getExpenseDateMapForGroupApi(group!!))
    }

    private suspend fun getExpenseCatMapForGroupApi(group: String): ExpenseMap {
        val originalMap: Map<GroupMapKey.StringKey, List<Expense>> = get(ExpenseEndpoint.GroupCatMap(group)).body()
        return originalMap.toMap(ExpenseMap(initialGroupingOrder = Order.ASCENDING))
    }

    fun getExpenseCatMapForGroup(group: String?) = flow {
        emit(getExpenseCatMapForGroupApi(group!!))
    }
}