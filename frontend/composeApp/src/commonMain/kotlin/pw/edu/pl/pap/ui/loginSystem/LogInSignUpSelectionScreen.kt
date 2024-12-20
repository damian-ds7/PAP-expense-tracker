package pw.edu.pl.pap.ui.loginSystem

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.unit.dp
import pw.edu.pl.pap.navigation.loginSystem.SelectionLoginSignupScreenComponent
import pw.edu.pl.pap.ui.common.TextButton

@Composable
fun LogInSignUpSelectionScreen (
    component: SelectionLoginSignupScreenComponent
){
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
        ){
            TextButton(
                text = "LOG IN",
                modifier = Modifier.offset(x = 0.dp, y = (-20).dp),
                onUpdate = component.onLogInButtonClicked
            )
            TextButton(
                text = "SIGN UP",
                modifier = Modifier.offset(x = 0.dp, y = 20.dp),
                onUpdate = component.onSignupButtonClicked
            )
        }
    }
}