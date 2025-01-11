package pw.edu.pl.pap.repositories.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pw.edu.pl.pap.data.databaseAssociatedData.Tokens

class TokenRepository {
    private var accessToken: String? = null

    private var refreshToken: String? = null


    fun setTokens(tokens: Tokens) {
        accessToken = tokens.accessToken
        refreshToken = tokens.refreshToken
    }

    fun getAccessToken(): String = accessToken!!
    fun getRefreshToken(): String = refreshToken!!
}