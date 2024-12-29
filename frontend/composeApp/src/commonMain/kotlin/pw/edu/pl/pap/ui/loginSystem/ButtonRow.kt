package pw.edu.pl.pap.ui.loginSystem

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pw.edu.pl.pap.screenComponents.loginSystem.BaseLoginScreenComponentImpl
import pw.edu.pl.pap.ui.common.TextButton

@Composable
fun LogInSignUpButtonRow(component: BaseLoginScreenComponentImpl, text: String, scope: CoroutineScope) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextButton(
            text = "BACK",
            modifier = Modifier.align(Alignment.BottomStart),
            onUpdate = { scope.launch { component.onBack() } }
        )

        TextButton(
            text = text,
            modifier = Modifier.align(Alignment.BottomEnd),
            onUpdate = { scope.launch { component.confirm() } }
        )
    }
}