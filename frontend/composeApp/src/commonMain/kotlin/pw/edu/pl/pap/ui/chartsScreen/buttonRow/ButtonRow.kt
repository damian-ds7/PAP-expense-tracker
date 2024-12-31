package pw.edu.pl.pap.ui.chartsScreen.buttonRow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pw.edu.pl.pap.screenComponents.mainScreens.ChartsScreenComponent
import pw.edu.pl.pap.ui.common.RefreshButton
import pw.edu.pl.pap.util.constants.horizontalPadding

@Composable
fun ButtonRow(
    component: ChartsScreenComponent,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = horizontalPadding),
        horizontalArrangement = Arrangement.End
    ) {
        FilterMenuButton { }
        RefreshButton { }
    }
}