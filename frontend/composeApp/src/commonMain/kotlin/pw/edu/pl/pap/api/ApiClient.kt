package pw.edu.pl.pap.api

import io.ktor.client.statement.*
import pw.edu.pl.pap.api.endpoints.ApiEndpoint

interface ApiClient {
    suspend fun get(endpoint: ApiEndpoint): HttpResponse

    suspend fun post(endpoint: ApiEndpoint, body: Any): HttpResponse

    suspend fun put(endpoint: ApiEndpoint, body: Any): HttpResponse

    suspend fun delete(endpoint: ApiEndpoint): HttpResponse
}