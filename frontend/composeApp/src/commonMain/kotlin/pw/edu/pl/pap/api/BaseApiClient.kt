package pw.edu.pl.pap.api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import pw.edu.pl.pap.api.endpoints.ApiEndpoint

open class BaseApiClient(
    private var baseUrl: String,
    private val httpClient: HttpClient,
    private val userToken: String
) : ApiClient {

    fun setUrl(newUrl: String) {
        //TODO api call to validate new address should be called here
        baseUrl = newUrl
    }

    fun getUrl(): String {
        return baseUrl
    }

    override suspend fun get(endpoint: ApiEndpoint): HttpResponse {
        return httpClient.get("$baseUrl${endpoint.path}") {
            contentType(ContentType.Application.Json)
            bearerAuth(userToken)
        }
    }

    override suspend fun post(endpoint: ApiEndpoint, body: Any): HttpResponse {
        return httpClient.post("$baseUrl${endpoint.path}") {
            contentType(ContentType.Application.Json)
            bearerAuth(userToken)
            setBody(body)
        }
    }

    override suspend fun put(endpoint: ApiEndpoint, body: Any): HttpResponse {
        return httpClient.put("$baseUrl${endpoint.path}") {
            contentType(ContentType.Application.Json)
            bearerAuth(userToken)
            setBody(body)
        }
    }

    override suspend fun delete(endpoint: ApiEndpoint): HttpResponse {
        return httpClient.delete("$baseUrl${endpoint.path}") {
            contentType(ContentType.Application.Json)
            bearerAuth(userToken)
        }
    }
}
