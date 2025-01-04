package pw.edu.pl.pap.util.dateFunctions

import kotlinx.datetime.*

fun getFirstAndLastDayOfAMonth(date: LocalDate): Pair<LocalDate, LocalDate> {
    val firstDayOfMonth = LocalDate(date.year, date.month, 1)
    val lastDayOfMonth =
        LocalDate(date.year, date.month, date.month.length(date.year % 4 == 0))
    return Pair(firstDayOfMonth, lastDayOfMonth)
}

fun getFirstAndLastDayOfAYear(date: LocalDate): Pair<LocalDate, LocalDate> {
    val firstDayOfYear = LocalDate(date.year, 1, 1)
    val lastDayOfYear = LocalDate(date.year, 12, 31)
    return Pair(firstDayOfYear, lastDayOfYear)
}