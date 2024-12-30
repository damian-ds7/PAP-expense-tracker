package pw.edu.pl.pap.api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

open class BaseApiClient(
    protected var baseUrl: String,
    private val httpClient: HttpClient,
    private val userToken: String
) {
    open fun setUrl(newUrl: String): Unit{
        throw NotImplementedError("Subclasses must override confirm")
    }

    fun getUrl(): String {
        return baseUrl
    }

    protected suspend fun get(endpoint: String): HttpResponse {
        return httpClient.get("$baseUrl$endpoint") {
            contentType(ContentType.Application.Json)
            bearerAuth(userToken)
        }
    }

    protected suspend fun post(endpoint: String, body: Any): HttpResponse {
        return httpClient.post("$baseUrl$endpoint") {
            contentType(ContentType.Application.Json)
            bearerAuth(userToken)
            setBody(body)
        }
    }

    protected suspend fun put(endpoint: String, body: Any): HttpResponse {
        return httpClient.put("$baseUrl$endpoint") {
            contentType(ContentType.Application.Json)
            bearerAuth(userToken)
            setBody(body)
        }
    }

    protected suspend fun delete(endpoint: String): HttpResponse {
        return httpClient.delete("$baseUrl$endpoint") {
            contentType(ContentType.Application.Json)
            bearerAuth(userToken)
        }
    }
}
