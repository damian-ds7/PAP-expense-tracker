package pw.edu.pl.pap.ui.chartsScreen.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.datetime.LocalDate
import pw.edu.pl.pap.screenComponents.mainScreens.ChartsScreenComponent
import pw.edu.pl.pap.ui.common.RefreshButton
import pw.edu.pl.pap.util.charts.FilterTimeFrames
import pw.edu.pl.pap.util.dateFunctions.formatDate

@Composable
fun ButtonRow(
    component: ChartsScreenComponent,
) {
    Row {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
            ) {
                FilterMenuButton { component.onFilterClick(component.chartFilters.value, component.currentUserGroup.value!!, "category") }
            }
            TimeFramePicker(component)
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
            ) {
                RefreshButton {
                    component.updateNavigationState(ChartsScreenComponent.NavigationState.InitialLoad)
                }
            }
        }
    }
}

@Composable
private fun TimeFramePicker(component: ChartsScreenComponent) {

    val timeFrame by component.currentTimeFrame.collectAsState()
    val timeBounds by component.currentTimeBounds.collectAsState()

    val text: @Composable () -> Unit = {
        Text(
            text = when (timeFrame) {
                FilterTimeFrames.MONTH -> formatDate(timeBounds.first!!, "MMMM yyyy")
                FilterTimeFrames.YEAR -> formatDate(timeBounds.first!!, "yyyy")
                else -> customTimeFrameText(timeBounds)
            },
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
    when (timeFrame) {
        FilterTimeFrames.MONTH, FilterTimeFrames.YEAR -> {
            MonthYearSwitcher(
                text = text,
                onLeftArrowClick = { component.modifyTimeBounds(-1) },
                onRightArrowClick = { component.modifyTimeBounds(1) },
            )
        }

        else -> {
            CustomTimeFrameDatePicker(
                text = text,
                timeBounds = timeBounds,
                modifyBoundsFunction = component::modifyTimeBounds,
            )
        }
    }
}

private fun customTimeFrameText(timeBounds: Pair<LocalDate?, LocalDate?>): String {
    val (beginDate, endDate) = timeBounds
    var text = ""

    if (beginDate == null && endDate == null) {
        return "All"
    }
    if (beginDate != null) {
        text += "$beginDate - "
    }
    if (endDate != null) {
        text += endDate.toString()
    }
    return text
}

