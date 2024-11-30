package pw.edu.pl.pap.apiclient

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.coroutines.flow.flow
import pw.edu.pl.pap.data.Home
import pw.edu.pl.pap.data.Record

class ApiClient {
    private val httpClient = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
    }

    private val baseUrl = "http://localhost:8080"

    suspend fun getHome(userEmail: String): Home {
        return httpClient.get("$baseUrl/expense/initial/$userEmail").body()
    }

    private suspend fun getRecordsApi(): List<Record> {
        return httpClient.get("$baseUrl/expense/all").body()
    }

    fun getRecords() = flow {
        emit(getRecordsApi())
    }
}