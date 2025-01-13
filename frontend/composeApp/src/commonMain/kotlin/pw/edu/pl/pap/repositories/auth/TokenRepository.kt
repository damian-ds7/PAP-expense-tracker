package pw.edu.pl.pap.repositories.auth

import io.ktor.client.call.*
import io.ktor.http.*
import pw.edu.pl.pap.api.auth.RefreshTokenApi
import pw.edu.pl.pap.data.databaseAssociatedData.RefreshToken
import pw.edu.pl.pap.data.databaseAssociatedData.Tokens
import pw.edu.pl.pap.storage.TokenStorage

class TokenRepository(private val storage: TokenStorage, private val refreshTokenApi: RefreshTokenApi) {
    private var accessToken: String? = null

    private var refreshToken: String? = null

    suspend fun checkRefreshToken(): Boolean {
        val refreshToken = storage.getToken() ?: return  false
        val response = refreshTokenApi.refresh(RefreshToken(refreshToken))
        if (response.status.isSuccess()) {
            val accessToken: String = response.body()
            setTokens(Tokens(accessToken, refreshToken))
            return true
        }
        return false
    }

    fun cleanTokens() {
        storage.clearToken()
    }

    fun setTokens(tokens: Tokens) {
        accessToken = tokens.accessToken
        refreshToken = tokens.refreshToken
        storage.saveToken(tokens.refreshToken)
    }

    fun getAccessToken(): String = accessToken!!
    fun getRefreshToken(): String = refreshToken!!
}