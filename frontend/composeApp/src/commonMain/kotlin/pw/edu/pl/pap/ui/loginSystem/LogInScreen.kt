package pw.edu.pl.pap.ui.loginSystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import pw.edu.pl.pap.screenComponents.loginSystem.LoginScreenComponent
import pw.edu.pl.pap.ui.common.*

@Composable
fun LogInScreen (
    component: LoginScreenComponent
) {
    val scope = rememberCoroutineScope()

    Header("Log In")
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
        mainText = "Log in failed",
        subText = component.failedLoginMessage.value,
        showWarning = component.showFailedLoginWarning.value,
        onDismiss = {
            component.showFailedLoginWarning.value = (!component.showFailedLoginWarning.value)
        }
    )

    ConfirmOrBackButtonRow(
        text = "LOG IN",
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