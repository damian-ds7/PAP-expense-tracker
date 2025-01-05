package pw.edu.pl.pap.api

import io.ktor.client.call.*
import pw.edu.pl.pap.api.endpoints.CurrencyEndpoint
import pw.edu.pl.pap.data.databaseAssociatedData.Currency

class CurrencyApiClient(baseApiClient: BaseApiClient) :
    ApiClient by baseApiClient {

    suspend fun getCurrencies(): List<Currency> {
        return get(CurrencyEndpoint.AllCurrencies).body()
    }
}