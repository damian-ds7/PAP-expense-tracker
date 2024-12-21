package pw.edu.pl.pap.api.authApi

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class LoginApi(private val baseUrl: String, private val httpClient: HttpClient) : UserAuthApi {

    override suspend fun post(body: Any): HttpResponse {
        return httpClient.post("$baseUrl/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }
}