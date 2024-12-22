package pw.edu.pl.pap.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.coroutines.flow.flow
import pw.edu.pl.pap.data.databaseAssociatedData.Expense
import pw.edu.pl.pap.data.databaseAssociatedData.NewExpense
import pw.edu.pl.pap.data.databaseAssociatedData.TotalExpenses
import pw.edu.pl.pap.util.sortingSystem.ExpenseMap
import pw.edu.pl.pap.util.sortingSystem.GroupMapKey
import pw.edu.pl.pap.util.sortingSystem.Order

class ExpenseApiClient(baseUrl: String, httpClient: HttpClient, userToken: String) :
    BaseApiClient(baseUrl, httpClient, userToken) {

    suspend fun getTotalExpenses(): TotalExpenses {
        return get("state").body()
    }

    private suspend fun getExpenseDateMapApi(): ExpenseMap {
        val originalMap: Map<GroupMapKey.DateKey, List<Expense>> = get("all/dateMap").body()
        return originalMap.toMap(ExpenseMap(initialGroupingOrder = Order.DESCENDING))
    }


    fun getExpenseDateMap() = flow {
        emit(getExpenseDateMapApi())
    }

    private suspend fun getExpenseCatMapApi(): ExpenseMap {
        val originalMap: Map<GroupMapKey.StringKey, List<Expense>> = get("all/categoryMap").body()
        return originalMap.toMap(ExpenseMap(initialGroupingOrder = Order.ASCENDING))
    }

    fun getExpenseCatMap() = flow {
        emit(getExpenseCatMapApi())
    }

    private suspend fun getExpenseApi(id: Long): Expense {
        return get("$id").body()
    }

    fun getExpense(id: Long) = flow {
        emit(getExpenseApi(id))
    }

    private suspend fun getRecentExpenseApi(): Expense {
        return get("recent").body()
    }

    fun getRecentExpense() = flow {
        emit(getRecentExpenseApi())
    }

    suspend fun updateExpense(expense: Expense): HttpResponse {
        return put("update/${expense.id}", expense)
    }

    suspend fun postNewExpense(newExpense: NewExpense) {
        println("expense to be uploaded  $newExpense")

        val response: HttpResponse = post("insert", newExpense)

        println("Response  " + response.body())
    }

    suspend fun deleteExpense(id: Long): HttpResponse {
        return delete("delete/$id")
    }
}