package pw.edu.pl.pap.api

import io.ktor.client.call.*
import io.ktor.client.statement.*
import pw.edu.pl.pap.api.endpoints.GroupEndpoint
import pw.edu.pl.pap.data.databaseAssociatedData.NewGroup
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup

class GroupApiClient(baseApiClient: BaseApiClient) :
    ApiClient by baseApiClient {

    suspend fun getUserGroups(): List<UserGroup> {
        return get(GroupEndpoint.GroupList).body()
    }

    suspend fun deleteGroup(id: Int): HttpResponse {
        return delete(GroupEndpoint.DeleteGroup(id))
    }

    suspend fun updateGroup(group: UserGroup): HttpResponse {
        return put(GroupEndpoint.UpdateGroup(group.id), group)
    }

    suspend fun postNewGroup(newGroup: NewGroup) {
        println("expense to be uploaded  $newGroup")

        val response: HttpResponse = post(GroupEndpoint.postNewGroup, newGroup)

        println("Response  " + response.body())
    }
}