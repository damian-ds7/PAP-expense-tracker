package pw.edu.pl.pap.api

import io.ktor.client.statement.*
import pw.edu.pl.pap.api.endpoints.TemporaryMembershipEndpoint

class TemporaryMembershipApiClient(baseApiClient: BaseApiClient) :
    ApiClient by baseApiClient {

    suspend fun accept(id: Long): HttpResponse {
        return post(TemporaryMembershipEndpoint.Accept(id))
    }

    suspend fun decline(id: Long): HttpResponse {
        return delete(TemporaryMembershipEndpoint.Decline(id))
    }

    suspend fun getReceivedInvitations() {
        //TODO
    }

    suspend fun getSentInvitations() {
        //TODO
    }
}