package pw.edu.pl.pap.api.data

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.statement.*
import pw.edu.pl.pap.data.databaseAssociatedData.InviteRequest

interface MembershipApi {

    @POST("membership/invite")
    suspend fun sendInvite(@Body request: InviteRequest): HttpResponse

}