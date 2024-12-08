package pw.edu.pl.pap.apiclient

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate
import pw.edu.pl.pap.data.NewExpense
import pw.edu.pl.pap.data.TotalExpenses
import pw.edu.pl.pap.data.Expense

class ApiClient(private val baseUrl: String = "http://localhost:8080") {
    private val httpClient = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getTotalExpenses(userGroup: String, userEmail: String): TotalExpenses {
        return httpClient.get("$baseUrl/expense/state/group/$userGroup/user/$userEmail").body()
    }

    private suspend fun getAllExpensesApi(): Map<LocalDate, List<Expense>> {
        return httpClient.get("$baseUrl/expense/all/dateMap").body()
    }

    fun getAllExpenses() = flow {
        emit(getAllExpensesApi())
    }

    suspend fun getExpense(id: Long): Expense {
        return httpClient.get("$baseUrl/expense/$id").body()
    }

    private suspend fun getRecentExpenseApi(): Expense {
        return httpClient.get("$baseUrl/expense/recent").body()
    }

    fun getRecentExpense() = flow {
        emit(getRecentExpenseApi())
    }

    suspend fun updateExpense(expense: Expense): HttpResponse {
        return httpClient.put("$baseUrl/expense/update/${expense.id}") {
            contentType(ContentType.Application.Json)
            setBody(expense)
        }
    }

    suspend fun postNewExpense(newExpense: NewExpense){

        println("expense to be uploaded  " + newExpense)

        val response: HttpResponse = httpClient.post("$baseUrl/expense/insert") {
            contentType(ContentType.Application.Json)
            setBody(newExpense)
        }

        println("Response  " + response.body())
    }


}