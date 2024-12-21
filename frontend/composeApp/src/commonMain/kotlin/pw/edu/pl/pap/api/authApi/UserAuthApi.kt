package pw.edu.pl.pap.api.authApi

import io.ktor.client.statement.*

interface UserAuthApi {
    suspend fun post(body: Any): HttpResponse
}