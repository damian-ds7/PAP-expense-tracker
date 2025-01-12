package pw.edu.pl.pap.api.data

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import io.ktor.client.statement.*
import pw.edu.pl.pap.data.databaseAssociatedData.NewPassword
import pw.edu.pl.pap.data.databaseAssociatedData.Preferences
import pw.edu.pl.pap.data.databaseAssociatedData.UpdatedUserData
import pw.edu.pl.pap.data.databaseAssociatedData.User

interface UserApi {

    @GET("user/current")
    suspend fun getCurrentUserInfo(): User

    @GET("user/search/{group}")
    suspend fun searchUsers(@Path("group") group: String, @Body user: List<String>): List<User>

    @GET("user/isAdmin/{group}")
    suspend fun isAdmin(@Path("group") group: String): Boolean

    @PUT("user/update")
    suspend fun updateUser(@Body user: UpdatedUserData): User

    @PUT("user/changePass")
    suspend fun changePassword(@Body newPass: NewPassword): HttpResponse

    @GET("preferences/current")
    suspend fun getCurrentPreferences(): Preferences

    @PUT("preferences/update")
    suspend fun updatePreferences(@Body newPreferences: Preferences): Preferences
}