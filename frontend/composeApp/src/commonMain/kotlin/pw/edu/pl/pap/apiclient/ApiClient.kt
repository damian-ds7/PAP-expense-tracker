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
import pw.edu.pl.pap.data.TotalExpenses
import pw.edu.pl.pap.data.Record

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

    private suspend fun getRecordsApi(): Map<LocalDate, List<Record>> {
        return httpClient.get("$baseUrl/expense/all/dateMap").body()
    }

    fun getRecords() = flow {
        emit(getRecordsApi())
    }

    suspend fun postNewExpense(record: Record){
        val response: HttpResponse = httpClient.post("$baseUrl/expense/insert") {
            contentType(ContentType.Application.Json)
            setBody(record)
        }
        println(response)
    }
}