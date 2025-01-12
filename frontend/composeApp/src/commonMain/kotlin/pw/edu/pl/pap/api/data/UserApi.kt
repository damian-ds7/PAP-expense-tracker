package pw.edu.pl.pap.api.data

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import pw.edu.pl.pap.data.databaseAssociatedData.User

interface UserApi {

    @GET("user/search/{group}")
    suspend fun searchUsers(@Path("group") group: String, @Body user: List<String>): List<User>

    @GET("user/isAdmin/{group}")
    suspend fun isAdmin(@Path("group") group: String): Boolean
}