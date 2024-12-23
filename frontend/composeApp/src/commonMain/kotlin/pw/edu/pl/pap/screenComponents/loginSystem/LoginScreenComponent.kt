package pw.edu.pl.pap.screenComponents.loginSystem

import com.arkivanov.decompose.ComponentContext
import io.ktor.client.call.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pw.edu.pl.pap.api.authApi.UserAuthApi
import pw.edu.pl.pap.data.databaseAssociatedData.UserLoginData

class LoginScreenComponent(
    componentContext: ComponentContext,
    coroutineScope: CoroutineScope,
    apiClient: UserAuthApi,
    onConfirm: () -> Unit,
    setToken: (String) -> Unit
) : BaseLoginScreenComponent(componentContext, coroutineScope, apiClient, onConfirm, setToken) {

    override fun confirm() {
//        val userLoginData = UserLoginData(email.value, password.value)
        val userLoginData = UserLoginData("herkules1@gmail.com", "123")

        coroutineScope.launch {
            val response = apiClient.post(userLoginData)
            val token = response.body<String>()
            if (response.status.isSuccess()) {
                setToken(token)
                onConfirm()
            } else {
                throw Exception("Login failed")
            }
        }
    }
}