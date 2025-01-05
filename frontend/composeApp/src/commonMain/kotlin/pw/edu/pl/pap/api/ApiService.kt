package pw.edu.pl.pap.api

import io.ktor.client.*

class ApiService(
    userToken: String,
    httpClient: HttpClient,
    baseUrl: String = "http://localhost:8080"
) {
    private val baseApiClient = BaseApiClient(baseUrl, httpClient, userToken)
    val expenseApiClient = ExpenseApiClient(baseApiClient)
    val groupApiClient = GroupApiClient(baseApiClient)
    val chartsApiClient = ChartsApiClient(baseApiClient)
    val currencyApiClient = CurrencyApiClient(baseApiClient)
    val categoryApiClient = CategoryApiClient(baseApiClient)
    val paymentMethodApiClient = PaymentMethodApiClient(baseApiClient)

    fun updateBaseUrl(newUrl: String) {
        baseApiClient.setUrl(newUrl)
    }

    fun getCurrentUrl(): String {
        return baseApiClient.getUrl()
    }
}