package pw.edu.pl.pap.ui.loginSystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pw.edu.pl.pap.navigation.loginSystem.SignupScreenComponent
import pw.edu.pl.pap.ui.common.Header
import pw.edu.pl.pap.ui.common.InputFields
import pw.edu.pl.pap.ui.common.TextButton
import pw.edu.pl.pap.ui.common.WarningPopup


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
        subText = "Passwords are not the same.",
        showWarning = component.showPasswordsWarning.value,
        onDismiss = {
            component.showPasswordsWarning.value = (!component.showPasswordsWarning.value)
        }
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter,
    ){
        TextButton(
            text = "SIGN UP",
            //        modifier = Modifier.align(Alignment.BottomCenter),
            onUpdate = { scope.launch { component.confirm() } }
        )
    }
}