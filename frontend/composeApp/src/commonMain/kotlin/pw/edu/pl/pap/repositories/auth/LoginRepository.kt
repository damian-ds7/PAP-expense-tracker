package pw.edu.pl.pap.repositories.auth

import io.ktor.client.call.*
import io.ktor.http.*
import pw.edu.pl.pap.api.auth.LoginApi
import pw.edu.pl.pap.data.databaseAssociatedData.Tokens
import pw.edu.pl.pap.data.databaseAssociatedData.UserLoginData

class LoginRepository(private val loginApi: LoginApi, private val tokenRepository: TokenRepository) {
    suspend fun login(userLoginData: UserLoginData): Boolean {
        val response = loginApi.login(userLoginData)

        //TODO add different return based on error
        if (!response.status.isSuccess()) {
            return false
        }

        tokenRepository.setTokens(response.body<Tokens>())
        return true
    }
}