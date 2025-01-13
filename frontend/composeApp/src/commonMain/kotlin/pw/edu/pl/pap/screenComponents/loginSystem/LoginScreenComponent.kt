package pw.edu.pl.pap.screenComponents.loginSystem

import kotlinx.coroutines.launch
import org.koin.core.component.inject
import pw.edu.pl.pap.data.databaseAssociatedData.UserLoginData
import pw.edu.pl.pap.repositories.auth.LoginRepository

class LoginScreenComponent(
    baseScreenComponent: BaseLoginScreenComponent,
) : BaseLoginScreenComponentImpl(baseScreenComponent) {

    private val loginRepository: LoginRepository by inject()

    override fun confirm() {
//        if (!validateEmail(email.value)) {
//            showEmailWarning.value = true
//            return
//        }
//        val userLoginData = UserLoginData(email.value, password.value)

        //temp
        val userLoginData = UserLoginData("herkules1@gmail.com", "123")


        coroutineScope.launch {
            val response = loginRepository.login(userLoginData)
            if (response) {
                onConfirm()
            } else {
                showFailedLoginWarning.value = true
                failedLoginMessage.value = "Something went wrong"
            }
        }
    }
}