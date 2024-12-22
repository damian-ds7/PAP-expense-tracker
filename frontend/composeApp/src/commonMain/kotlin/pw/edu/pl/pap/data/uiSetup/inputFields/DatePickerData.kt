package pw.edu.pl.pap.data.uiSetup.inputFields

import androidx.compose.runtime.MutableState
import kotlinx.datetime.LocalDate

data class DatePickerData (
    val date: MutableState<LocalDate>,
    val onDateConfirm: (Long) -> Unit
)