package pw.edu.pl.pap.api

import io.ktor.client.call.*
import pw.edu.pl.pap.api.endpoints.GroupEndpoint
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup

class GroupApiClient(baseApiClient: BaseApiClient) :
    ApiClient by baseApiClient {

    suspend fun getUserGroups(): List<UserGroup> {
        return get(GroupEndpoint.GroupList).body()
    }

    suspend fun getUsersInGroup(group: String): List<User> {
        return get(GroupEndpoint.UserList(group)).body()
    }
}