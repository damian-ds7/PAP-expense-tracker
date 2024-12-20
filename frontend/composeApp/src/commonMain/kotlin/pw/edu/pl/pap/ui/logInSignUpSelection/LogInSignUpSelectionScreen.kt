package pw.edu.pl.pap.ui.logInSignUpSelection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        LazyColumn(
            modifier = Modifier
        ){
            TextButton(
                text = "LOG IN",
                onUpdate = component.onLogInButtonClicked
            )
            TextButton(
                text = "SIGN UP",
                onUpdate = component.onSignupButtonClicked
            )
        }
    }
}