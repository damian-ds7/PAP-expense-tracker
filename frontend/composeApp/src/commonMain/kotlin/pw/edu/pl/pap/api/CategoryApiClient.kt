package pw.edu.pl.pap.api

import io.ktor.client.call.*
import pw.edu.pl.pap.api.endpoints.CategoryEndpoint
import pw.edu.pl.pap.data.databaseAssociatedData.Category

class CategoryApiClient(baseApiClient: BaseApiClient) :
    ApiClient by baseApiClient {

    suspend fun getCategories(): List<Category> {
        return get(CategoryEndpoint.AllCategories).body()
    }
}