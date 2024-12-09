package pw.edu.pl.pap.data.inputFields

import androidx.compose.runtime.MutableState
import kotlinx.datetime.LocalDate

data class DatePickerData (
    val date: MutableState<LocalDate>,
    val onDateConfirm: (Long) -> Unit
)