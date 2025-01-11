package pw.edu.pl.pap.api.data

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.statement.*
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup

interface MembershipApi {

    @POST("membership/invite")
    suspend fun updateGroup(@Body user: User, group: UserGroup): HttpResponse

}