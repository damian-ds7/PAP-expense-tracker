package pw.edu.pl.pap.ui.chartsScreen.menus

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import pw.edu.pl.pap.screenComponents.mainScreens.ChartsScreenComponent
import pw.edu.pl.pap.util.charts.FilterTimeFrames
import pw.edu.pl.pap.util.constants.verticalPadding

@Composable
fun TabBar(component: ChartsScreenComponent) {
    val selectedTab by component.currentTimeFrame.collectAsState()
    val tabs = FilterTimeFrames.entries

    TabRow(
        selectedTabIndex = tabs.indexOf(selectedTab),
        modifier = Modifier.fillMaxWidth().padding(bottom = verticalPadding)
    ) {
        tabs.forEachIndexed { _, tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { component.changeTimeFrame(tab) },
                text = { Text(tab.displayName) })
        }
    }
}