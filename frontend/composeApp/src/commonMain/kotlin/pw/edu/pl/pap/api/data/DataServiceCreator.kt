package pw.edu.pl.pap.api.data

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import pw.edu.pl.pap.data.databaseAssociatedData.RefreshToken
import pw.edu.pl.pap.repositories.auth.TokenRepository

class DataServiceCreator(tokenRepository: TokenRepository, baseUrl: String) {
    private val bearerTokenStorage =
        mutableListOf(BearerTokens(tokenRepository.getAccessToken(), tokenRepository.getRefreshToken()))

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
        install(Auth) {
            bearer {
                loadTokens {
                    bearerTokenStorage.last()
                }
                refreshTokens {
                    val url = "$baseUrl/auth/refresh"
                    val body = RefreshToken(oldTokens?.refreshToken ?: "")
                    val newAccessToken: String = client.post(url) {
                        contentType(ContentType.Application.Json)
                        setBody(body)
                    }.body()
                    bearerTokenStorage.add(BearerTokens(newAccessToken, oldTokens?.refreshToken!!))
                    bearerTokenStorage.last()
                }

            }
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 3000
        }
        install(HttpCache)
    }

    private val ktorfit = Ktorfit.Builder()
        .baseUrl(baseUrl)
        .httpClient(httpClient)
        .build()

    fun createExpenseApi(): ExpenseApi {
        return ktorfit.createExpenseApi()
    }

    fun createGroupApi(): GroupApi {
        return ktorfit.createGroupApi()
    }

    fun createChartApi(): ChartApi {
        return ktorfit.createChartApi()
    }

    fun createConfigApi(): ConfigApi {
        return ktorfit.createConfigApi()
    }
}