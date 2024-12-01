
package pw.edu.pl.pap.apiclient

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.coroutines.flow.flow
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

    private suspend fun getRecordsApi(): List<Record> {
        return httpClient.get("$baseUrl/expense/all").body()
    }

    fun getRecords() = flow {
        emit(getRecordsApi())
    }
}