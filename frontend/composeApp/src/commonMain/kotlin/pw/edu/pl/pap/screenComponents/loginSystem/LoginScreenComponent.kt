package pw.edu.pl.pap.screenComponents.loginSystem

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
            val response = loginRepository.login(userLoginData)
            if (response) {
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