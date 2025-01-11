package pw.edu.pl.pap.api.auth

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.statement.*
import pw.edu.pl.pap.data.databaseAssociatedData.UserLoginData

interface LoginApi {
    @POST("auth/login")
    suspend fun login(@Body loginData: UserLoginData): HttpResponse
}