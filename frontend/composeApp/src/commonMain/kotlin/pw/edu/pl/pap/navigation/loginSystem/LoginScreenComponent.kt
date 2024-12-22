package pw.edu.pl.pap.navigation.loginSystem

import com.arkivanov.decompose.ComponentContext
import io.ktor.client.call.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pw.edu.pl.pap.api.authApi.UserAuthApi
import pw.edu.pl.pap.data.databaseAssociatedData.UserLoginData
import pw.edu.pl.pap.util.validateEmail

class LoginScreenComponent(
    componentContext: ComponentContext,
    coroutineScope: CoroutineScope,
    apiClient: UserAuthApi,
    onConfirm: () -> Unit,
    setToken: (String) -> Unit
) : BaseLoginScreenComponent(componentContext, coroutineScope, apiClient, onConfirm, setToken) {

    override fun confirm() {
//        if (!validateEmail(email.value)) {
//            showEmailWarning.value = true
//            return
//        }
//        val userLoginData = UserLoginData(email.value, password.value)
        val userLoginData = UserLoginData("herkules1@gmail.com", "123")

        coroutineScope.launch {
            val response = apiClient.post(userLoginData)
            val token = response.body<String>()
            if (response.status.isSuccess()) {
                setToken(token)
                onConfirm()
            } else {
                showFailedLoginWarning.value = true
                //TODO set failedLoginMessage
                failedLoginMessage.value = "Something went wrong"
            }
        }
    }
}