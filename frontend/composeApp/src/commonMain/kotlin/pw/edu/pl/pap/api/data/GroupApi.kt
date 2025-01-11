package pw.edu.pl.pap.api.data

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import io.ktor.client.statement.*
import pw.edu.pl.pap.data.databaseAssociatedData.NewGroup
import pw.edu.pl.pap.data.databaseAssociatedData.User
import pw.edu.pl.pap.data.databaseAssociatedData.UserGroup


interface GroupApi {

    @GET("group/all")
    suspend fun getUserGroups(): List<UserGroup>

    @GET("group/members/{group}")
    suspend fun getUsersInGroup(@Path("group") group: String): List<User>

    @GET("group/{id}")
    suspend fun getGroup(@Path("id") id: Int): UserGroup

    @DELETE("group/delete/{id}")
    suspend fun deleteGroup(@Path("id") id: Int): HttpResponse

    @PUT("group/update/{id}")
    suspend fun updateGroup(@Path("id") id: Int, @Body group: UserGroup): HttpResponse

    @POST("group/insert")
    suspend fun postNewGroup(@Body newGroup: NewGroup): HttpResponse
}