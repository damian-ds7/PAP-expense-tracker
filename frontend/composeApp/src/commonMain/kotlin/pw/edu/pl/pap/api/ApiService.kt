package pw.edu.pl.pap.api

import io.ktor.client.*

class ApiService(
    private val userToken: String,
    private val httpClient: HttpClient,
    baseUrl: String = "http://localhost:8080"
) {

    val expenseApiClient = ExpenseApiClient("$baseUrl/expense/", httpClient, userToken)
    val groupApiClient = GroupApiClient("$baseUrl/group/", httpClient, userToken)
}