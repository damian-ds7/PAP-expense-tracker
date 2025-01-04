package pw.edu.pl.pap.util.dateFunctions

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

fun getCurrentDate(): LocalDate {
    return Clock.System.todayIn(TimeZone.UTC)
}