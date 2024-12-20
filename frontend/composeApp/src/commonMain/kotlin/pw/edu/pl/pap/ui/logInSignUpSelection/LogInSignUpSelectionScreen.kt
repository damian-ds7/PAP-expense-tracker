package pw.edu.pl.pap.ui.logInSignUpSelection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pw.edu.pl.pap.navigation.loginSystem.SelectionLoginSignupScreenComponent

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
            LogInSignUpButton(
                text = "LOG IN",
                onUpdate = component.onLogInButtonClicked
            )
            LogInSignUpButton(
                text = "SIGN UP",
                onUpdate = component.onSignupButtonClicked
            )
        }
    }
}