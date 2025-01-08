package pw.edu.pl.pap.api

import io.ktor.client.call.*
import pw.edu.pl.pap.api.endpoints.UserEndpoints
import pw.edu.pl.pap.data.databaseAssociatedData.User

class UserApiClient(baseApiClient: BaseApiClient) :
    ApiClient by baseApiClient {

    suspend fun search(group: String, name: String, surname:String): List<User>{
        return get(UserEndpoints.Search(group), listOf(name, surname)).body()
    }
}