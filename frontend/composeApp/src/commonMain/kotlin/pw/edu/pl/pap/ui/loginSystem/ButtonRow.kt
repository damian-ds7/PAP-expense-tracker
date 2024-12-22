package pw.edu.pl.pap.ui.loginSystem

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pw.edu.pl.pap.navigation.HomeScreenComponent
import pw.edu.pl.pap.navigation.loginSystem.BaseLoginScreenComponent
import pw.edu.pl.pap.ui.common.TextButton
import pw.edu.pl.pap.ui.home.sortingSystem.GroupButton
import pw.edu.pl.pap.ui.home.sortingSystem.RefreshButton

@Composable
fun LogInSignUpButtonRow(component: BaseLoginScreenComponent, text: String, scope: CoroutineScope) {

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