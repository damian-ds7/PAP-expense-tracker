package pw.edu.pl.pap.ui.loginSystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pw.edu.pl.pap.navigation.loginSystem.LoginScreenComponent
import pw.edu.pl.pap.ui.common.Header
import pw.edu.pl.pap.ui.common.InputFields
import pw.edu.pl.pap.ui.common.TextButton
import pw.edu.pl.pap.ui.common.WarningPopup

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ){
        TextButton(
            text = "LOG IN",
        //        modifier = Modifier.align(Alignment.BottomCenter),
            onUpdate = { scope.launch { component.confirm() } }
        )
    }
}