package pw.edu.pl.pap.api.data

import de.jensklingenberg.ktorfit.http.*
import io.ktor.client.statement.*
import pw.edu.pl.pap.data.databaseAssociatedData.Invitation

interface TemporaryMembershipApi {

    @POST("tempMembership/accept/{id}")
    suspend fun accept(@Path("id")  id: Long): HttpResponse

    @DELETE("tempMembership/delete/{id}")
    suspend fun decline(@Path("id")  id: Long): HttpResponse

    @GET("tempMembership/invitations")
    suspend fun getReceived(): List<Invitation>

    @GET("tempMembership/sentInvitations")
    suspend fun getSent(): List<Invitation>
}