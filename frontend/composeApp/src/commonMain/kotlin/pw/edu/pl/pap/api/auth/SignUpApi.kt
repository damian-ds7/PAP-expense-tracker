package pw.edu.pl.pap.api.auth

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.statement.*
import pw.edu.pl.pap.data.databaseAssociatedData.UserSignUpData

interface SignUpApi {
    @POST("auth/register")
    suspend fun signUp(@Body signUpData: UserSignUpData): HttpResponse
}