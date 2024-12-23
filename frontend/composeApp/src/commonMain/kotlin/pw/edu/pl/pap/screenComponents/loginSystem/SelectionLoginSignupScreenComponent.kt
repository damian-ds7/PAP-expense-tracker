package pw.edu.pl.pap.screenComponents.loginSystem

import com.arkivanov.decompose.ComponentContext

class SelectionLoginSignupScreenComponent(
    componentContext: ComponentContext,
    val onLogInButtonClicked: () -> Unit,
    val onSignupButtonClicked: () -> Unit
) : ComponentContext by componentContext {
    //TODO show log in button
    //TODO show Sign up button
}