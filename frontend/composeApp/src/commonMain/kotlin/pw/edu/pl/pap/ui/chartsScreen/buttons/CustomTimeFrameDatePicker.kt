package pw.edu.pl.pap.ui.chartsScreen.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.datetime.LocalDate
import pw.edu.pl.pap.util.dateFunctions.dateToMillis

@Composable
fun CustomTimeFrameDatePicker(
    text: @Composable () -> Unit,
    timeBounds: Pair<LocalDate?, LocalDate?>,
    modifyBoundsFunction: (LocalDate?, LocalDate?) -> Unit,
) {
    val (beginDate, endDate) = timeBounds
    var showDatePicker by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .clickable { showDatePicker = true },
        contentAlignment = Alignment.Center
    )
    {
        text()
    }

    if (showDatePicker) {
        DatePicker(
            beginDate,
            endDate,
            onConfirm = { newBeginDate, newEndDate ->
                modifyBoundsFunction(newBeginDate, newEndDate)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePicker(
    beginDate: LocalDate?,
    endDate: LocalDate?,
    onConfirm: (LocalDate?, LocalDate?) -> Unit,
    onDismiss: () -> Unit
) {

    val datePickerState = rememberDateRangePickerState(dateToMillis(beginDate), dateToMillis(endDate))

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val startMillis = datePickerState.selectedStartDateMillis
                val endMillis = datePickerState.selectedEndDateMillis
                val newBeginDate = startMillis?.let { LocalDate.fromEpochDays((it / (24 * 60 * 60 * 1000)).toInt()) }
                val newEndDate = endMillis?.let { LocalDate.fromEpochDays((it / (24 * 60 * 60 * 1000)).toInt()) }
                onConfirm(newBeginDate, newEndDate)
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        content = {
            Box(modifier = Modifier.weight(1f)) {
                DateRangePicker(state = datePickerState)
            }
        }
    )
}