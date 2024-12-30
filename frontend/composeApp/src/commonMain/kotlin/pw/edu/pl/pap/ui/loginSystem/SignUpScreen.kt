package pw.edu.pl.pap.ui.loginSystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import pw.edu.pl.pap.screenComponents.loginSystem.SignupScreenComponent
import pw.edu.pl.pap.ui.common.*


@Composable
fun SignUpScreen (
    component: SignupScreenComponent
) {
    val scope = rememberCoroutineScope()

    Header("Sign Up")
    component.setupInputFields()
    InputFields(component.inputFieldsData)

    WarningPopup(
        subText = "Incorrect email.",
        showWarning = component.showEmailWarning.value,
        onDismiss = {
            component.showEmailWarning.value = (!component.showEmailWarning.value)
        }
    )

    WarningPopup(
        subText = "Incorrect passwords.",
        showWarning = component.showPasswordsWarning.value,
        onDismiss = {
            component.showPasswordsWarning.value = (!component.showPasswordsWarning.value)
        }
    )

    WarningPopup(
        mainText = "Sign up failed",
        subText = component.failedLoginMessage.value,
        showWarning = component.showFailedLoginWarning.value,
        onDismiss = {
            component.showFailedLoginWarning.value = (!component.showFailedLoginWarning.value)
        }
    )



    ConfirmOrBackButtonRow(
        text = "SIGN UP",
        onBack = {
            component.coroutineScope.launch {
                component.onBack()
            }
        },
        onConfirm = {
            component.coroutineScope.launch {
                component.confirm()
            }
        }
    )
}