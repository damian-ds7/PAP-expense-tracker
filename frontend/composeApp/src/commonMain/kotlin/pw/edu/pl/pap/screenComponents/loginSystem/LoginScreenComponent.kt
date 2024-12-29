package pw.edu.pl.pap.screenComponents.loginSystem

import io.ktor.client.call.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import pw.edu.pl.pap.data.databaseAssociatedData.UserLoginData

class LoginScreenComponent(
    baseScreenComponent: BaseLoginScreenComponent,
) : BaseLoginScreenComponentImpl(baseScreenComponent) {

    override fun confirm() {
//        if (!validateEmail(email.value)) {
//            showEmailWarning.value = true
//            return
//        }
//        val userLoginData = UserLoginData(email.value, password.value)

        //temp
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
                //temp
                failedLoginMessage.value = "Something went wrong"
            }
        }
    }
}